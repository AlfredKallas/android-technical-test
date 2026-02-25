package fr.leboncoin.ui.ui

import androidx.compose.runtime.Immutable

@Immutable
data class AlbumUIModel(
    val id: Long,
    val albumId: Long,
    val title: String,
    val thumbnailUrl: String,
    val isFavourite: Boolean = false,
)
