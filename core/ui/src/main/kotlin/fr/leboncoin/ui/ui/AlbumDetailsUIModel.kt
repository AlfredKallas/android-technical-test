package fr.leboncoin.ui.ui

import androidx.compose.runtime.Immutable

@Immutable
data class AlbumDetailsUIModel(
    val id: Long,
    val albumId: Long,
    val title: String,
    val url: String,
    val isFavourite: Boolean = false,
)
