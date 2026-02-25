package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.snackbars.SnackbarHostState
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.androidrecruitmenttestapp.navigation.NavigationManager
import fr.leboncoin.androidrecruitmenttestapp.navigation.Navigator
import fr.leboncoin.ui.compositionlocal.LocalSnackbarHostState
import fr.leboncoin.ui.navigation.AlbumDetailsEntry
import fr.leboncoin.ui.navigation.AlbumDetailsNavKey
import fr.leboncoin.ui.navigation.AlbumsListEntry
import fr.leboncoin.ui.navigation.AlbumsNavKey
import fr.leboncoin.ui.navigation.FavouritesListEntry
import fr.leboncoin.ui.navigation.FavouritesNavKey
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class,
        ExperimentalSparkApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val backstack: NavBackStack<NavKey> = rememberNavBackStack(AlbumsNavKey)
            val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
            val navigator = remember { Navigator(backstack) }
            val snackbarHostState = remember { SnackbarHostState() }

            val entryProvider = entryProvider {
                AlbumsListEntry(
                    onItemSelected = { id ->
                        navigator.navigate(AlbumDetailsNavKey(id.toLong()))
                    },
                    onFavouritesClick = {
                        navigator.navigate(FavouritesNavKey)
                    }
                )
                AlbumDetailsEntry(
                    onBack = { navigator.goBack() }
                )
                FavouritesListEntry(
                    onItemSelected = { id ->
                        navigator.navigate(AlbumDetailsNavKey(id.toLong()))
                    },
                    onBack = { navigator.goBack() }
                )
            }

            SparkTheme {
                CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                    NavDisplay(
                        modifier = Modifier.fillMaxSize(),
                        backStack = backstack,
                        sceneStrategy = listDetailStrategy,
                        onBack = { navigator.goBack() },
                        entryProvider = entryProvider
                    )
                }
            }
        }
    }
}