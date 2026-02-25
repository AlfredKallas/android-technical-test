package fr.leboncoin.ui.mapper

import fr.leboncoin.data.model.Album
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumUIMapperTest {

    private val mapper = AlbumUIMapperImpl()

    private val sampleAlbum = Album(
        id = 1L,
        albumId = 10L,
        title = "Test Album",
        url = "https://example.com/full.jpg",
        thumbnailUrl = "https://example.com/thumb.jpg",
        isFavourite = true
    )

    @Test
    fun `toAlbumUIModel maps correctly`() {
        val uiModel = mapper.toAlbumUIModel(sampleAlbum)

        assertEquals(sampleAlbum.id, uiModel.id)
        assertEquals(sampleAlbum.albumId, uiModel.albumId)
        assertEquals(sampleAlbum.title, uiModel.title)
        assertEquals(sampleAlbum.thumbnailUrl, uiModel.thumbnailUrl)
        assertEquals(sampleAlbum.isFavourite, uiModel.isFavourite)
    }

    @Test
    fun `toAlbumDetailsUIModel maps correctly`() {
        val detailsUiModel = mapper.toAlbumDetailsUIModel(sampleAlbum)

        assertEquals(sampleAlbum.id, detailsUiModel.id)
        assertEquals(sampleAlbum.albumId, detailsUiModel.albumId)
        assertEquals(sampleAlbum.title, detailsUiModel.title)
        assertEquals(sampleAlbum.url, detailsUiModel.url)
        assertEquals(sampleAlbum.isFavourite, detailsUiModel.isFavourite)
    }
}
