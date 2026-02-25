package fr.leboncoin.ui

import app.cash.turbine.test
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.data.repository.AnalyticsEventsRepository
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
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: AlbumRepository = mock()
    private val analyticsRepository: AnalyticsEventsRepository = mock()
    private val albumMapper: AlbumUIMapper = mock()

    private lateinit var viewModel: AlbumsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(repository.getAlbums()).thenReturn(flowOf())
        whenever(repository.sync()).thenReturn(flowOf(LCResult.Success(Unit)))
        viewModel = AlbumsViewModel(repository, analyticsRepository, albumMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAlbums updates syncState to Success`() = runTest {
        whenever(repository.sync()).thenReturn(flowOf(LCResult.Loading, LCResult.Success(Unit)))
        viewModel = AlbumsViewModel(repository, analyticsRepository, albumMapper)
        
        viewModel.syncState.test {
            assertEquals(SyncState.Loading, awaitItem())
            assertEquals(SyncState.Success, awaitItem())
        }
    }

    @Test
    fun `loadAlbums updates syncState to Error`() = runTest {
        val errorMessage = "Network Error"
        whenever(repository.sync()).thenReturn(flowOf(LCResult.Loading, LCResult.Error(Exception(errorMessage))))
        viewModel = AlbumsViewModel(repository, analyticsRepository, albumMapper)
        
        viewModel.syncState.test {
            assertEquals(SyncState.Loading, awaitItem())
            val state = awaitItem()
            assertTrue(state is SyncState.Error)
            assertEquals(errorMessage, (state as SyncState.Error).message)
        }
    }

    @Test
    fun `trackEventOnItemSelected calls analytics repository`() {
        val id = "123"
        viewModel.trackEventOnItemSelected(id)

        verify(analyticsRepository).logEvent(any())
    }

    @Test
    fun `toggleFavourite emits snackbar event`() = runTest {
        val album = AlbumUIModel(1L, 10L, "Title", "thumb", false)
        
        viewModel.snackbarEvent.test {
            viewModel.toggleFavourite(album)
            val event = awaitItem()
            assertTrue(event is UiText.StringResource)
            assertEquals(R.string.added_to_favourites, (event as UiText.StringResource).resId)
        }
    }
}
