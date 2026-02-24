package fr.leboncoin.androidrecruitmenttestapp.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.leboncoin.androidrecruitmenttestapp.navigation.MainDestination
import fr.leboncoin.androidrecruitmenttestapp.navigation.NavigationManager

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AlbumListDetailScreen(
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<MainDestination>()

    // Handle global navigation events (safe navigation + debounced clicks)
    navigationManager.HandleNavigationEvents { destination ->
         when (destination) {
            is MainDestination.AlbumList -> navigator.navigateBack()
            is MainDestination.AlbumDetail -> navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, destination)
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AlbumListContent(
                onAlbumClick = { id ->
                    navigationManager.navigateTo(MainDestination.AlbumDetail(id))
                }
            )
        },
        detailPane = {
            val destination = navigator.currentDestination?.content
            if (destination is MainDestination.AlbumDetail) {
                AlbumDetailContent(albumId = destination.id)
            } else {
                // Placeholder or welcome screen
            }
        },
        modifier = modifier
    )
}

@Composable
fun AlbumListContent(
    onAlbumClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // UI placeholder
}

@Composable
fun AlbumDetailContent(
    albumId: Long,
    modifier: Modifier = Modifier
) {
    // UI placeholder
}
