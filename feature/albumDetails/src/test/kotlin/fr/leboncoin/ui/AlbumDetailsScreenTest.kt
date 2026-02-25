package fr.leboncoin.ui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.snackbars.SnackbarHostState
import fr.leboncoin.ui.compositionlocal.LocalSnackbarHostState
import fr.leboncoin.ui.ui.AlbumDetailsUIModel
import fr.leboncoin.ui.util.TestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class AlbumDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val snackbarHostState = SnackbarHostState()

    @Test
    fun `when state is Loading then show LoadingScreen`() {
        // Act
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                SparkTheme {
                    AlbumDetailsScreen(
                        albumDetailsState = AlbumDetailsState.Loading,
                        onToggleFavourite = {},
                        onBack = {},
                        onRetry = {}
                    )
                }
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(TestTags.ALBUM_DETAILS_LOADING).assertIsDisplayed()
    }

    @Test
    fun `when state is Error then show ErrorScreen`() {
        // Act
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                SparkTheme {
                    AlbumDetailsScreen(
                        albumDetailsState = AlbumDetailsState.Error("Failed to load"),
                        onToggleFavourite = {},
                        onBack = {},
                        onRetry = {}
                    )
                }
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(TestTags.ERROR_SCREEN).assertIsDisplayed()
    }

    @Test
    fun `when state is Success then show SuccessScreen`() {
        // Arrange
        val album = AlbumDetailsUIModel(
            id = 1,
            albumId = 1,
            title = "Detail Album 1",
            url = "http://example.com/url",
            isFavourite = false
        )

        // Act
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                SparkTheme {
                    AlbumDetailsScreen(
                        albumDetailsState = AlbumDetailsState.Success(album),
                        onToggleFavourite = {},
                        onBack = {},
                        onRetry = {}
                    )
                }
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(TestTags.ALBUM_DETAILS_SCREEN).assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Detail Album 1", substring = true).onFirst().assertIsDisplayed()
    }
}
