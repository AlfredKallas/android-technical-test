package fr.leboncoin.ui.ui

import androidx.compose.runtime.Stable

@Stable
data class AlbumDetailsUIModel(
    val id: Long,
    val albumId: Long,
    val title: String,
    val url: String,
)
