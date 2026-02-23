package fr.leboncoin.network.repository

import fr.leboncoin.network.api.AlbumApiService

class AlbumRepository(
    private val albumApiService: AlbumApiService,
) {
    
    suspend fun getAllAlbums() = albumApiService.getAlbums()
}