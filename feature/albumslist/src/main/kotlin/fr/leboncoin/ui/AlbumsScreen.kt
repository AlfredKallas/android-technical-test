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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.components.progress.CircularProgressIndicator
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.ui.pagingdsl.HandlePagingItems
import fr.leboncoin.ui.pagingdsl.StablePagingItems
import fr.leboncoin.ui.screens.EmptyScreen
import fr.leboncoin.ui.screens.ErrorScreen
import fr.leboncoin.ui.ui.AlbumUIModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    stablePagingItems: StablePagingItems<AlbumUIModel>,
    syncState: SyncState,
    onItemSelected : (AlbumUIModel) -> Unit,
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
                        is SyncState.Loading -> SongsLoadingScreen()
                        is SyncState.Error -> ErrorScreen(
                            message = syncState.message,
                            onRetry = onRetry
                        )
                        is SyncState.Success -> EmptyScreen(onRetry = onRetry)
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