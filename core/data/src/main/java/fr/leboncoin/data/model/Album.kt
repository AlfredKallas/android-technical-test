package fr.leboncoin.data.model

import javax.annotation.concurrent.Immutable

@Immutable
data class Album(
    val id: Long,
    val albumId: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)