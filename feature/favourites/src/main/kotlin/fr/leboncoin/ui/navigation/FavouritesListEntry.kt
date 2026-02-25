package fr.leboncoin.ui.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import fr.leboncoin.ui.FavouritesScreen
import fr.leboncoin.ui.FavouritesViewModel
import fr.leboncoin.ui.pagingdsl.StablePagingItems

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.FavouritesListEntry(
    onItemSelected: (String) -> Unit,
    onBack: () -> Unit,
) {
    entry<FavouritesNavKey>(
        metadata = ListDetailSceneStrategy.listPane(),
    ) {
        val viewModel = hiltViewModel<FavouritesViewModel>()
        val pagingItems = viewModel.paginationFlow.collectAsLazyPagingItems()

        val stableItems = remember(pagingItems) { StablePagingItems(pagingItems) }

        FavouritesScreen(
            stablePagingItems = stableItems,
            snackbarEvent = viewModel.snackbarEvent,
            onItemSelected = {
                onItemSelected.invoke(it.id.toString())
            },
            onToggleFavourite = viewModel::toggleFavourite,
            onBack = onBack
        )
    }
}
