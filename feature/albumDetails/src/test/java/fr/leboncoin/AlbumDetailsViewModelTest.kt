package fr.leboncoin

import app.cash.turbine.test
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.data.model.Album
import fr.leboncoin.data.repository.AlbumRepository
import fr.leboncoin.resources.R
import fr.leboncoin.ui.AlbumDetailsState
import fr.leboncoin.ui.AlbumDetailsViewModel
import fr.leboncoin.ui.mapper.AlbumUIMapper
import fr.leboncoin.ui.ui.AlbumDetailsUIModel
import fr.leboncoin.ui.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: AlbumRepository = mock()
    private val albumMapper: AlbumUIMapper = mock()
    private val albumId = 1L

    private lateinit var viewModel: AlbumDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(repository.getAlbumDetails(albumId)).thenReturn(flowOf(LCResult.Loading))
        viewModel = AlbumDetailsViewModel(repository, albumMapper, albumId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAlbumDetails updates state to Success`() = runTest {
        val album = mock<Album>()
        val uiModel = AlbumDetailsUIModel(albumId, 10L, "Title", "url", false)

        whenever(repository.getAlbumDetails(albumId)).thenReturn(
            flowOf(
                LCResult.Loading,
                LCResult.Success(album)
            )
        )
        whenever(albumMapper.toAlbumDetailsUIModel(album)).thenReturn(uiModel)

        viewModel.state.test {
            Assert.assertEquals(AlbumDetailsState.Loading, awaitItem()) // Initial from stateIn
            val state = awaitItem()
            Assert.assertTrue(state is AlbumDetailsState.Success)
            Assert.assertEquals(uiModel, (state as AlbumDetailsState.Success).album)
        }
    }

    @Test
    fun `toggleFavourite emits snackbar event`() = runTest {
        val album = AlbumDetailsUIModel(albumId, 10L, "Title", "url", false)

        viewModel.snackbarEvent.test {
            viewModel.toggleFavourite(album)
            val event = awaitItem()
            Assert.assertTrue(event is UiText.StringResource)
            Assert.assertEquals(
                R.string.added_to_favourites,
                (event as UiText.StringResource).resId
            )
        }
    }
}