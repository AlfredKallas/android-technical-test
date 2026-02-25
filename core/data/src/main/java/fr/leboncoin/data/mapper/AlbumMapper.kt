package fr.leboncoin.data.mapper

import fr.leboncoin.data.model.Album
import fr.leboncoin.database.model.AlbumEntity
import fr.leboncoin.network.model.AlbumDto
import javax.inject.Inject

interface AlbumMapper {
    fun toAlbumWithSong(albumWithSongsEntity: AlbumEntity): Album

    fun toAlbumEntity(albumDto: AlbumDto): AlbumEntity

    fun toListAlbumEntity(albumDtoList: List<AlbumDto>): List<AlbumEntity>
}

class AlbumMapperImpl @Inject constructor() : AlbumMapper {
    override fun toAlbumWithSong(albumWithSongsEntity: AlbumEntity): Album =
        Album(
            id = albumWithSongsEntity.id,
            albumId = albumWithSongsEntity.albumId,
            title = albumWithSongsEntity.title,
            url = albumWithSongsEntity.url,
            thumbnailUrl = albumWithSongsEntity.thumbnailUrl,
            isFavourite = albumWithSongsEntity.isFavourite
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