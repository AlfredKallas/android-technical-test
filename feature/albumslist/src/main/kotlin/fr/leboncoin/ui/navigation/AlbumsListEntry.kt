package fr.leboncoin.ui.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import fr.leboncoin.ui.AlbumsScreen
import fr.leboncoin.ui.AlbumsViewModel
import fr.leboncoin.ui.compositionlocal.LocalSnackbarHostState
import fr.leboncoin.ui.pagingdsl.StablePagingItems

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.AlbumsListEntry(
    onItemSelected: (String) -> Unit,
    onFavouritesClick: () -> Unit,
) {
    entry<AlbumsNavKey>(
        metadata = ListDetailSceneStrategy.listPane(),
    ) {
        val viewModel = hiltViewModel<AlbumsViewModel>()
        val pagingItems = viewModel.paginationFlow.collectAsLazyPagingItems()

        val stableItems = remember(pagingItems) { StablePagingItems(pagingItems) }

        val syncState by viewModel.syncState.collectAsStateWithLifecycle()

        val snackbarHostState = LocalSnackbarHostState.current
        val context = LocalContext.current
        LaunchedEffect(viewModel.snackbarEvent) {
            viewModel.snackbarEvent.collect {
                snackbarHostState.showSnackbar(it.asString(context))
            }
        }

        AlbumsScreen(
            stablePagingItems = stableItems,
            syncState = syncState,
            onItemSelected = {
                viewModel.trackEventOnItemSelected(it.id.toString())
                onItemSelected.invoke(it.id.toString())
            },
            onToggleFavourite = viewModel::toggleFavourite,
            onFavouritesClick = onFavouritesClick,
            onRetry = viewModel::loadAlbums
        )
    }
}