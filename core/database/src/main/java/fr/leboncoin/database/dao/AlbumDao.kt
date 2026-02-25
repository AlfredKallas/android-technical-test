package fr.leboncoin.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.leboncoin.database.model.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM albums where id = :albumId")
    fun getAlbumDetails(albumId: Long): Flow<AlbumEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)

    @Query("UPDATE albums SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updateFavourite(id: Long, isFavourite: Boolean)

    @Query("SELECT id FROM albums WHERE isFavourite = 1")
    suspend fun getFavouriteIds(): List<Long>

    @Query("SELECT * FROM albums WHERE isFavourite = 1")
    fun getFavouritesPaged(): PagingSource<Int, AlbumEntity>

    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    @Query("SELECT * FROM albums")
    fun getAlbumsWithSongsPaged(): PagingSource<Int, AlbumEntity>
}
