package fr.leboncoin.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: Long,
    val albumId: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
    val isFavourite: Boolean = false
)
