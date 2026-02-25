package fr.leboncoin.ui

import app.cash.turbine.test
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.resources.R
import fr.leboncoin.ui.mapper.AlbumUIMapper
import fr.leboncoin.ui.ui.AlbumUIModel
import fr.leboncoin.ui.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: AlbumRepository = mock()
    private val albumMapper: AlbumUIMapper = mock()

    private lateinit var viewModel: FavouritesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(repository.getFavourites()).thenReturn(flowOf())
        viewModel = FavouritesViewModel(repository, albumMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `toggleFavourite emits snackbar event`() = runTest {
        val album = AlbumUIModel(1L, 10L, "Title", "thumb", true)
        
        viewModel.snackbarEvent.test {
            viewModel.toggleFavourite(album)
            val event = awaitItem()
            assertTrue(event is UiText.StringResource)
            assertEquals(R.string.removed_from_favourites, (event as UiText.StringResource).resId)
        }
    }
}
