package fr.leboncoin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.chips.ChipTinted
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.text.Text
import fr.leboncoin.ui.extensions.shimmer
import fr.leboncoin.ui.screens.ErrorScreen
import fr.leboncoin.ui.ui.AlbumDetailsUIModel

@Composable
fun AlbumDetailsScreen(
    albumDetailsState: AlbumDetailsState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (albumDetailsState) {
                is AlbumDetailsState.Loading -> AlbumDetailsLoadingScreen()
                is AlbumDetailsState.Error -> ErrorScreen(
                    message = albumDetailsState.message,
                    onRetry = onRetry
                )
                is AlbumDetailsState.Success -> AlbumDetailsSuccessScreen(albumDetailsState.album)
            }
        }
    }
}

@Composable
fun AlbumDetailsSuccessScreen(album: AlbumDetailsUIModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var loading by remember { mutableStateOf(true) }

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shimmer(loading),
            onSuccess = { loading = false },
            onError = { loading = false },
            model = ImageRequest.Builder(LocalContext.current)
                .data(album.url)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = album.title,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = album.title,
                style = SparkTheme.typography.headline1
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipTinted(text = "Album #${album.albumId}")
                ChipTinted(text = "Track #${album.id}")
            }
        }
    }
}

@Composable
fun AlbumDetailsLoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shimmer(true)
        )

        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(32.dp)
                    .shimmer(true)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth(0.3f)
                        .shimmer(true)
                )
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth(0.3f)
                        .shimmer(true)
                )
            }
        }
    }
}