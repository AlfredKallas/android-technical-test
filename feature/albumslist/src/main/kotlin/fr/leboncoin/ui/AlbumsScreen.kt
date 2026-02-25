package fr.leboncoin.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.adevinta.spark.components.progress.CircularProgressIndicator
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.data.model.Album
import fr.leboncoin.ui.pagingdsl.HandlePagingItems
import fr.leboncoin.ui.pagingdsl.StablePagingItems
import fr.leboncoin.ui.screens.EmptyScreen
import fr.leboncoin.ui.screens.ErrorScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    onItemSelected : (Album) -> Unit,
) {
    val viewModel: AlbumsViewModel = hiltViewModel()
    val pagingItems = viewModel.paginationFlow.collectAsLazyPagingItems()
    val syncState by viewModel.syncState.collectAsState()

    val stableItems = remember(pagingItems) { StablePagingItems(pagingItems) }

    AlbumsScreen(
        modifier = modifier,
        stablePagingItems = stableItems,
        syncState = syncState,
        onItemSelected = onItemSelected,
        onRetry = { viewModel.loadAlbums() }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    stablePagingItems: StablePagingItems<Album>,
    syncState: LCResult<Unit>,
    onItemSelected : (Album) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            val dpCacheWindow = LazyLayoutCacheWindow(ahead = 150.dp, behind = 150.dp)

            val songsListState = rememberLazyListState(
                cacheWindow = dpCacheWindow
            )

            HandlePagingItems(stablePagingItems) {
                onRefresh { SongsLoadingScreen() }
                onError { error ->
                    ErrorScreen(
                        message = error.message.orEmpty(),
                        onRetry = {
                            stablePagingItems.items.retry()
                            onRetry()
                        }
                    )
                }
                onEmpty {
                    when (syncState) {
                        is LCResult.Loading -> SongsLoadingScreen()
                        is LCResult.Error -> ErrorScreen(
                            message = "We couldn't synchronize the albums. Please check your connection.",
                            onRetry = onRetry
                        )
                        is LCResult.Success -> EmptyScreen(onRetry = onRetry)
                    }
                }
                onSuccess { _ ->
                    LazyColumn(
                        state = songsListState,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        onPagingItems(key = { it.id }) { _, album ->
                            AlbumItem(
                                album = album,
                                onItemSelected = onItemSelected,
                            )
                        }
                        onAppendItem { CircularProgressIndicator(progress = { 1f }, Modifier.padding(6.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun SongsLoadingScreen() {
    LazyColumn(
        userScrollEnabled = false,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(10) {
            AlbumItemPlaceHolder()
        }
    }
}