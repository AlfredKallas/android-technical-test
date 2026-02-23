package fr.leboncoin.network.api

import fr.leboncoin.network.model.AlbumDto
import retrofit2.http.GET

interface AlbumApiService {
    
    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumDto>
}