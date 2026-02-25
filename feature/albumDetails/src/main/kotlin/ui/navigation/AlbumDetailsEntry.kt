package fr.leboncoin.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import fr.leboncoin.ui.AlbumDetailsScreen
import fr.leboncoin.ui.AlbumDetailsViewModel
import fr.leboncoin.ui.compositionlocal.LocalSnackbarHostState

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.AlbumDetailsEntry(
    onBack: () -> Unit,
) {
    entry<AlbumDetailsNavKey>(
        metadata = ListDetailSceneStrategy.detailPane(),
    ) { key ->

        val viewModel: AlbumDetailsViewModel = hiltViewModel<AlbumDetailsViewModel, AlbumDetailsViewModel.Factory>(
            key = key.id.toString(),
        ) { factory ->
            factory.create(key.id)
        }

        val state by viewModel.state.collectAsStateWithLifecycle()

        val snackbarHostState = LocalSnackbarHostState.current
        val context = LocalContext.current
        LaunchedEffect(viewModel.snackbarEvent) {
            viewModel.snackbarEvent.collect {
                snackbarHostState.showSnackbar(it.asString(context))
            }
        }

        AlbumDetailsScreen(
            modifier = Modifier.fillMaxSize(),
            albumDetailsState = state,
            onToggleFavourite = viewModel::toggleFavourite,
            onBack = onBack,
            onRetry = {
                viewModel.loadAlbumDetails()
            }
        )
    }
}