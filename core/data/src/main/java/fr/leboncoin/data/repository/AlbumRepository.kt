package fr.leboncoin.data.repository

import fr.leboncoin.network.api.AlbumApiService
import fr.leboncoin.network.model.AlbumDto
import javax.inject.Inject

interface AlbumRepository {
    suspend fun getAllAlbums(): List<AlbumDto>
}

internal class OfflineFirstAlbumRepository @Inject constructor(
    private val albumApiService: AlbumApiService,
): AlbumRepository {
    
    override suspend fun getAllAlbums() = albumApiService.getAlbums()
}