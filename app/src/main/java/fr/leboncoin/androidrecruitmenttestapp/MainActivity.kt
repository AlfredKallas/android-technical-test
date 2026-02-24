package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.adevinta.spark.SparkTheme
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.androidrecruitmenttestapp.navigation.MainDestination
import fr.leboncoin.androidrecruitmenttestapp.navigation.NavigationManager
import fr.leboncoin.androidrecruitmenttestapp.navigation.Navigator
import fr.leboncoin.androidrecruitmenttestapp.ui.AlbumListContent
import fr.leboncoin.androidrecruitmenttestapp.ui.AlbumListDetailScreen
import fr.leboncoin.ui.AlbumsScreen
import fr.leboncoin.ui.navigation.AlbumsListEntry
import fr.leboncoin.ui.navigation.AlbumsNavKey
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val backstack = rememberNavBackStack(AlbumsNavKey)

            val navigator = remember { Navigator(backstack) }

            val entryProvider = entryProvider {
                AlbumsListEntry(onItemSelected = {
                    //navigator.navigate() We need to add the Album Details Screen
                })

                bookmarksEntry(navigator)
                interestsEntry(navigator)
                topicEntry(navigator)
                searchEntry(navigator)
            }

            SparkTheme {
                NavDisplay(
                    backstack = backstack,
                ) { entry ->
                    when (val destination = entry.key) {
                        is MainDestination.AlbumList -> {
                            NavEntry(key = destination) {
                                AlbumsScreen(
                                    onItemSelected = { album ->
                                        navigationManager.navigateTo(MainDestination.AlbumDetail(album.id))
                                    },

                                )
                            }
                        }
                        else -> {
                             NavEntry(key = destination) {
                                // Fallback
                            }
                        }
                    }
                }
            }
        }
    }
}