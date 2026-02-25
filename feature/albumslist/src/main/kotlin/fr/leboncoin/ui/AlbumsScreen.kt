package fr.leboncoin.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.appbar.TopAppBar
import com.adevinta.spark.components.iconbuttons.IconButtonGhost
import com.adevinta.spark.components.progress.CircularProgressIndicator
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.StarOutline
import fr.leboncoin.resources.R
import fr.leboncoin.ui.components.AlbumItem
import fr.leboncoin.ui.components.AlbumsLoadingScreen
import fr.leboncoin.ui.compositionlocal.LocalSnackbarHostState
import fr.leboncoin.ui.pagingdsl.HandlePagingItems
import fr.leboncoin.ui.pagingdsl.StablePagingItems
import fr.leboncoin.ui.screens.EmptyScreen
import fr.leboncoin.ui.screens.ErrorScreen
import fr.leboncoin.ui.ui.AlbumUIModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalSparkApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    stablePagingItems: StablePagingItems<AlbumUIModel>,
    syncState: SyncState,
    onItemSelected : (AlbumUIModel) -> Unit,
    onToggleFavourite: (AlbumUIModel) -> Unit,
    onFavouritesClick: () -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = LocalSnackbarHostState.current
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.albums_title)) },
                actions = {
                    IconButtonGhost(
                        icon = SparkIcons.StarOutline,
                        contentDescription = stringResource(R.string.favourites_title),
                        onClick = onFavouritesClick
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            val dpCacheWindow = LazyLayoutCacheWindow(ahead = 150.dp, behind = 150.dp)

            val songsListState = rememberLazyListState(
                cacheWindow = dpCacheWindow
            )

            HandlePagingItems(stablePagingItems) {
                onRefresh { AlbumsLoadingScreen() }
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
                        is SyncState.Loading -> AlbumsLoadingScreen()
                        is SyncState.Error -> ErrorScreen(
                            message = syncState.message,
                            onRetry = onRetry
                        )
                        is SyncState.Success -> EmptyScreen(text = stringResource(R.string.no_albums), onRetry = onRetry)
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
                                onToggleFavourite = onToggleFavourite
                            )
                        }
                        onAppendItem { CircularProgressIndicator(progress = { 1f }, Modifier.padding(6.dp)) }
                    }
                }
            }
        }
    }
}

