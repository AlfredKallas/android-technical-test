package fr.leboncoin.ui.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import fr.leboncoin.ui.AlbumsScreen
import fr.leboncoin.ui.AlbumsViewModel
import fr.leboncoin.ui.pagingdsl.StablePagingItems

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.AlbumsListEntry(
    onItemSelected: (String) -> Unit,
) {
    entry<AlbumsNavKey>(
        metadata = ListDetailSceneStrategy.listPane(),
    ) { key ->
        val viewModel = hiltViewModel<AlbumsViewModel>()
        val pagingItems = viewModel.paginationFlow.collectAsLazyPagingItems()

        val stableItems = remember(pagingItems) { StablePagingItems(pagingItems) }

        AlbumsScreen(
            stablePagingItems = stableItems,
            onItemSelected = {
                viewModel.trackEventOnItemSelected(it.id.toString())
                onItemSelected.invoke(it.id.toString())
            }
        )
    }
}