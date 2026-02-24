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
    fun getAlbumDetails(albumId: Long): AlbumEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)

    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    @Query("SELECT * FROM albums")
    fun getAlbumsWithSongsPaged(): PagingSource<Int, AlbumEntity>
}
