package fr.leboncoin.data.repository

import app.cash.turbine.test
import fr.leboncoin.common.result.DomainError
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.data.mapper.AlbumMapper
import fr.leboncoin.data.model.Album
import fr.leboncoin.database.LeboncoinDatabase
import fr.leboncoin.database.dao.AlbumDao
import fr.leboncoin.database.model.AlbumEntity
import fr.leboncoin.network.api.AlbumApiService
import fr.leboncoin.network.model.AlbumDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val albumApiService: AlbumApiService = mock()
    private val database: LeboncoinDatabase = mock()
    private val albumDao: AlbumDao = mock()
    private val albumMapper: AlbumMapper = mock()

    private lateinit var repository: OfflineFirstAlbumRepository

    @Before
    fun setup() {
        whenever(database.albumDao()).thenReturn(albumDao)
        repository = OfflineFirstAlbumRepository(
            coroutineDispatcher = testDispatcher,
            albumApiService = albumApiService,
            database = database,
            albumMapper = albumMapper
        )
    }

    @Test
    fun `sync success inserts albums into database`() = runTest(testDispatcher) {
        val dtos = listOf(AlbumDto(1, 10, "Title", "url", "thumb"))
        val entities = listOf(AlbumEntity(1, 10, "Title", "url", "thumb", false))
        
        whenever(albumApiService.getAlbums()).thenReturn(dtos)
        whenever(albumDao.getFavouriteIds()).thenReturn(emptyList())
        whenever(albumMapper.toListAlbumEntity(dtos)).thenReturn(entities)

        repository.sync().test {
            assertTrue(awaitItem() is LCResult.Loading)
            assertTrue(awaitItem() is LCResult.Success)
            awaitComplete()
        }

        verify(albumDao).insertAlbums(entities)
    }

    @Test
    fun `sync preserves favorite status`() = runTest(testDispatcher) {
        val dtos = listOf(AlbumDto(1, 10, "Title", "url", "thumb"))
        val entities = listOf(AlbumEntity(1, 10, "Title", "url", "thumb", false))
        val favoriteEntities = listOf(AlbumEntity(1, 10, "Title", "url", "thumb", true))
        
        whenever(albumApiService.getAlbums()).thenReturn(dtos)
        whenever(albumDao.getFavouriteIds()).thenReturn(listOf(1L))
        whenever(albumMapper.toListAlbumEntity(dtos)).thenReturn(entities)

        repository.sync().test {
            awaitItem()
            awaitItem()
            awaitComplete()
        }

        verify(albumDao).insertAlbums(favoriteEntities)
    }

    @Test
    fun `toggleFavourite updates database`() = runTest(testDispatcher) {
        repository.toggleFavourite(1L, true)
        verify(albumDao).updateFavourite(1L, true)
    }

    @Test
    fun `getAlbumDetails returns mapped album from database`() = runTest(testDispatcher) {
        val entity = AlbumEntity(1, 10, "Title", "url", "thumb", false)
        val album = Album(1, 10, "Title", "url", "thumb", false)

        whenever(albumDao.getAlbumDetails(1L)).thenReturn(flowOf(entity))
        whenever(albumMapper.toAlbumWithSong(entity)).thenReturn(album)

        repository.getAlbumDetails(1L).test {
            assertTrue(awaitItem() is LCResult.Loading)
            val result = awaitItem()
            assertTrue(result is LCResult.Success)
            assertEquals(album, (result as LCResult.Success).data)
            awaitComplete()
        }
    }
    @Test
    fun `getAlbumDetails returns Error with DomainError NotFound when album not in database`() = runTest(testDispatcher) {
        whenever(albumDao.getAlbumDetails(1L)).thenReturn(flowOf(null))

        repository.getAlbumDetails(1L).test {
            assertTrue(awaitItem() is LCResult.Loading)
            val result = awaitItem()
            assertTrue(result is LCResult.Error)
            assertTrue((result as LCResult.Error).exception is DomainError.NotFound)
            assertEquals(1L, ((result as LCResult.Error).exception as DomainError.NotFound).id)
            awaitComplete()
        }
    }
}
