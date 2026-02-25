package fr.leboncoin.ui.mapper

import fr.leboncoin.data.model.Album
import fr.leboncoin.ui.ui.AlbumDetailsUIModel
import fr.leboncoin.ui.ui.AlbumUIModel
import javax.inject.Inject

interface AlbumUIMapper {
    fun toAlbumUIModel(album: Album): AlbumUIModel

    fun toAlbumDetailsUIModel(album: Album): AlbumDetailsUIModel
}

class AlbumUIMapperImpl @Inject constructor() : AlbumUIMapper {
    override fun toAlbumUIModel(album: Album): AlbumUIModel =
        AlbumUIModel(
            id = album.id,
            albumId = album.albumId,
            title = album.title,
            thumbnailUrl = album.thumbnailUrl
        )
    override fun toAlbumDetailsUIModel(album: Album): AlbumDetailsUIModel =
        AlbumDetailsUIModel(
            id = album.id,
            albumId = album.albumId,
            title = album.title,
            url = album.url
        )
}