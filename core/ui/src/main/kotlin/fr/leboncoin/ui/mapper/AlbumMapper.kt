package fr.leboncoin.ui.mapper

import fr.leboncoin.ui.ui.AlbumUIModel
import javax.inject.Inject

interface AlbumUIMapper {
    fun toAlbumUIModel(album: Album): AlbumUIModel

    fun toAlbumEntity(albumDto: AlbumDto): AlbumEntity

    fun toListAlbumEntity(albumDtoList: List<AlbumDto>): List<AlbumEntity>
}

class AlbumUIMapperImpl @Inject constructor() : AlbumUIMapper {
    override fun toAlbumWithSong(albumWithSongsEntity: AlbumEntity): Album =
        Album(
            id = albumWithSongsEntity.id,
            albumId = albumWithSongsEntity.albumId,
            title = albumWithSongsEntity.title,
            url = albumWithSongsEntity.url,
            thumbnailUrl = albumWithSongsEntity.thumbnailUrl
        )

    override fun toAlbumEntity(albumDto: AlbumDto): AlbumEntity =
        AlbumEntity(
            id = albumDto.id.toLong(),
            albumId = albumDto.albumId.toLong(),
            title = albumDto.title,
            url = albumDto.url,
            thumbnailUrl = albumDto.thumbnailUrl
        )

    override fun toListAlbumEntity(albumDtoList: List<AlbumDto>): List<AlbumEntity> =
        albumDtoList.map { toAlbumEntity(it) }
}