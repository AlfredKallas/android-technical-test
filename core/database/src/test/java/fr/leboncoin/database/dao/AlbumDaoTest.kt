package fr.leboncoin.database.dao

import fr.leboncoin.database.model.AlbumEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class AlbumDaoTest : DatabaseTest() {

    @Test
    fun `GIVEN an album WHEN inserting it THEN it should be in the database`() = runTest {
        // GIVEN
        val album = AlbumEntity(1, 1, "title", "url", "thumbnailUrl", false)

        // WHEN
        albumDao.insertAlbums(listOf(album))

        // THEN
        val result = albumDao.getAlbumDetails(1).first()
        assertEquals(album, result)
    }

    @Test
    fun `GIVEN multiple albums WHEN inserting them THEN they should all be in the database`() = runTest {
        // GIVEN
        val album1 = AlbumEntity(1, 1, "title1", "url1", "thumbnailUrl1", false)
        val album2 = AlbumEntity(2, 1, "title2", "url2", "thumbnailUrl2", false)

        // WHEN
        albumDao.insertAlbums(listOf(album1, album2))

        // THEN
        val result1 = albumDao.getAlbumDetails(1).first()
        val result2 = albumDao.getAlbumDetails(2).first()
        assertEquals(album1, result1)
        assertEquals(album2, result2)
    }

    @Test
    fun `GIVEN an album WHEN updating its favourite status THEN the change should be reflected`() = runTest {
        // GIVEN
        val album = AlbumEntity(1, 1, "title", "url", "thumbnailUrl", false)
        albumDao.insertAlbums(listOf(album))
        assertEquals(false, albumDao.getAlbumDetails(1).first()?.isFavourite)

        // WHEN
        albumDao.updateFavourite(1, true)

        // THEN
        assertEquals(true, albumDao.getAlbumDetails(1).first()?.isFavourite)
    }

    @Test
    fun `GIVEN favourited albums WHEN getting favourite ids THEN the correct ids are returned`() = runTest {
        // GIVEN
        val album1 = AlbumEntity(1, 1, "title1", "url1", "thumbnailUrl1", true)
        val album2 = AlbumEntity(2, 1, "title2", "url2", "thumbnailUrl2", false)
        val album3 = AlbumEntity(3, 1, "title3", "url3", "thumbnailUrl3", true)
        albumDao.insertAlbums(listOf(album1, album2, album3))

        // WHEN
        val favouriteIds = albumDao.getFavouriteIds()

        // THEN
        assertEquals(2, favouriteIds.size)
        assertTrue(favouriteIds.contains(1L))
        assertTrue(favouriteIds.contains(3L))
    }

    @Test
    fun `GIVEN albums in the database WHEN deleteAllAlbums THEN the database should be empty`() = runTest {
        // GIVEN
        val album = AlbumEntity(1, 1, "title", "url", "thumbnailUrl", false)
        albumDao.insertAlbums(listOf(album))
        assertEquals(album, albumDao.getAlbumDetails(1).first())

        // WHEN
        albumDao.deleteAllAlbums()

        // THEN
        assertNull(albumDao.getAlbumDetails(1).first())
    }
}
