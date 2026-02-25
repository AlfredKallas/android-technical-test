package fr.leboncoin.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import fr.leboncoin.common.network.ARTDispatchers
import fr.leboncoin.common.network.Dispatcher
import fr.leboncoin.common.result.LCResult
import fr.leboncoin.common.result.NetworkError
import fr.leboncoin.common.result.asResult
import fr.leboncoin.common.result.mapOnError
import fr.leboncoin.common.result.mapSuccess
import fr.leboncoin.common.result.mapToUnitOnSuccess
import fr.leboncoin.common.result.onSuccess
import fr.leboncoin.data.mapper.AlbumMapper
import fr.leboncoin.data.model.Album
import fr.leboncoin.database.LeboncoinDatabase
import fr.leboncoin.network.api.AlbumApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

interface AlbumRepository {
    fun getAlbums(): Flow<PagingData<Album>>
    fun getAlbumDetails(id: Long): Flow<LCResult<Album>>
    fun sync(): Flow<LCResult<Unit>>
}

internal class OfflineFirstAlbumRepository @Inject constructor(
    @Dispatcher(ARTDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
    private val albumApiService: AlbumApiService,
    private val database: LeboncoinDatabase,
    private val albumMapper: AlbumMapper
): AlbumRepository {

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    override fun getAlbums(): Flow<PagingData<Album>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // DB page size (how many rows to read from DB at a time)
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { database.albumDao().getAlbumsWithSongsPaged() }
        ).flow
            .map { pagingData ->
                pagingData.map { albumEntity ->
                    albumMapper.toAlbumWithSong(albumEntity)
                }
            }
    }

    override fun sync(): Flow<LCResult<Unit>> = flow {
        val albums = albumApiService.getAlbums()
        emit(albums)
    }.asResult().map { result ->
        result.onSuccess {
            database.albumDao().insertAlbums(albumMapper.toListAlbumEntity(it))
        }.mapToUnitOnSuccess().mapOnError {
            if (it is HttpException) {
                NetworkError.HttpError(it.code(), it.message())
            } else NetworkError.UnknownError(it)
        }
    }.flowOn(coroutineDispatcher)

    override fun getAlbumDetails(id: Long): Flow<LCResult<Album>> = flow {
        val albumEntity = database.albumDao().getAlbumDetails(id)
        if (albumEntity != null) {
            emit(albumEntity)
        } else {
            throw Exception("Album not found in database for id: $id")
        }
    }.asResult().map { result ->
        result.mapSuccess {
            albumMapper.toAlbumWithSong(it)
        }.mapOnError {
            NetworkError.UnknownError(it)
        }
    }.flowOn(coroutineDispatcher)
}