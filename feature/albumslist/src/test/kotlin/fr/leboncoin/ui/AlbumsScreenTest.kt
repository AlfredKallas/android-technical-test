package fr.leboncoin.ui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.snackbars.SnackbarHostState
import fr.leboncoin.ui.compositionlocal.LocalSnackbarHostState
import fr.leboncoin.ui.pagingdsl.StablePagingItems
import fr.leboncoin.ui.ui.AlbumUIModel
import fr.leboncoin.ui.util.TestTags
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class AlbumsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val snackbarHostState = SnackbarHostState()

    @Test
    fun `when syncState is Loading and paging has no items then show LoadingScreen`() {
        // Arrange
        val pagingDataFlow = MutableStateFlow(PagingData.empty<AlbumUIModel>())

        // Act
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                SparkTheme {
                    val items = pagingDataFlow.collectAsLazyPagingItems()
                    AlbumsScreen(
                        stablePagingItems = StablePagingItems(items),
                        syncState = SyncState.Loading,
                        onItemSelected = {},
                        onToggleFavourite = {},
                        onFavouritesClick = {},
                        onRetry = {}
                    )
                }
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(TestTags.LOADING_SCREEN).assertIsDisplayed()
    }

    @Test
    fun `when syncState is Error and paging has no items then show ErrorScreen`() {
        // Arrange
        val pagingDataFlow = MutableStateFlow(PagingData.empty<AlbumUIModel>())

        // Act
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                SparkTheme {
                    val items = pagingDataFlow.collectAsLazyPagingItems()
                    AlbumsScreen(
                        stablePagingItems = StablePagingItems(items),
                        syncState = SyncState.Error("Network Error"),
                        onItemSelected = {},
                        onToggleFavourite = {},
                        onFavouritesClick = {},
                        onRetry = {}
                    )
                }
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(TestTags.ERROR_SCREEN).assertIsDisplayed()
    }

    @Test
    fun `when syncState is Success but paging has no items then show EmptyScreen`() {
        // Arrange
        val pagingDataFlow = MutableStateFlow(PagingData.from(
            data = emptyList<AlbumUIModel>(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = true),
                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                append = LoadState.NotLoading(endOfPaginationReached = true)
            )
        ))

        // Act
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                SparkTheme {
                    val items = pagingDataFlow.collectAsLazyPagingItems()
                    AlbumsScreen(
                        stablePagingItems = StablePagingItems(items),
                        syncState = SyncState.Success,
                        onItemSelected = {},
                        onToggleFavourite = {},
                        onFavouritesClick = {},
                        onRetry = {}
                    )
                }
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(TestTags.EMPTY_SCREEN).assertIsDisplayed()
    }

    @Test
    fun `when paging has items then show AlbumList`() {
        // Arrange
        val albums = listOf(
            AlbumUIModel(id = 1, albumId = 1, title = "Album 1", thumbnailUrl = "", isFavourite = false),
            AlbumUIModel(id = 2, albumId = 1, title = "Album 2", thumbnailUrl = "", isFavourite = true)
        )
        val pagingDataFlow = MutableStateFlow(PagingData.from(albums))

        // Act
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                SparkTheme {
                    val items = pagingDataFlow.collectAsLazyPagingItems()
                    AlbumsScreen(
                        stablePagingItems = StablePagingItems(items),
                        syncState = SyncState.Success,
                        onItemSelected = {},
                        onToggleFavourite = {},
                        onFavouritesClick = {},
                        onRetry = {}
                    )
                }
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(TestTags.ALBUM_LIST).assertIsDisplayed()
        composeTestRule.onNodeWithTag("${TestTags.ALBUM_ITEM}1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("${TestTags.ALBUM_ITEM}2").assertIsDisplayed()
    }
}
