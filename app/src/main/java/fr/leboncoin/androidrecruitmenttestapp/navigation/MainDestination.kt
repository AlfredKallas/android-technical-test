package fr.leboncoin.androidrecruitmenttestapp.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
sealed interface MainDestination {
    @Serializable
    data object AlbumList : MainDestination
    
    @Serializable
    data class AlbumDetail(val id: Long) : MainDestination
}
