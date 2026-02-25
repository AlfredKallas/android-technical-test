package fr.leboncoin.ui.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import fr.leboncoin.ui.FavouritesScreen
import fr.leboncoin.ui.FavouritesViewModel
import fr.leboncoin.ui.compositionlocal.LocalSnackbarHostState
import fr.leboncoin.ui.pagingdsl.StablePagingItems

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.FavouritesListEntry(
    onItemSelected: (String) -> Unit,
    onBack: () -> Unit,
) {
    entry<FavouritesNavKey> {
        val viewModel = hiltViewModel<FavouritesViewModel>()
        val pagingItems = viewModel.paginationFlow.collectAsLazyPagingItems()

        val stableItems = remember(pagingItems) { StablePagingItems(pagingItems) }

        val snackbarHostState = LocalSnackbarHostState.current
        val context = LocalContext.current
        LaunchedEffect(viewModel.snackbarEvent) {
            viewModel.snackbarEvent.collect {
                snackbarHostState.showSnackbar(it.asString(context))
            }
        }

        FavouritesScreen(
            stablePagingItems = stableItems,
            onItemSelected = {
                onItemSelected.invoke(it.id.toString())
            },
            onToggleFavourite = viewModel::toggleFavourite,
            onBack = onBack
        )
    }
}
