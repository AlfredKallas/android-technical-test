package fr.leboncoin.data.repository

import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.data.network.model.AlbumDto

interface AlbumRepository {
    suspend fun getAllAlbums(): List<AlbumDto>
}

internal class OfflineFirstAlbumRepository(
    private val albumApiService: AlbumApiService,
): AlbumRepository {
    
    override suspend fun getAllAlbums() = albumApiService.getAlbums()
}