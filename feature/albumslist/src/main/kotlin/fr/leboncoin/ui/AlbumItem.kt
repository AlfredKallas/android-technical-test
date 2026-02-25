package fr.leboncoin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.card.Card
import com.adevinta.spark.components.chips.ChipTinted
import com.adevinta.spark.components.text.Text
import fr.leboncoin.ui.extensions.shimmer
import fr.leboncoin.ui.ui.AlbumUIModel
import timber.log.Timber

@OptIn(ExperimentalSparkApi::class)
@Composable
fun AlbumItem(
    album: AlbumUIModel,
    onItemSelected : (AlbumUIModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp),
        onClick = { onItemSelected(album) },
    ) {
        Row {
            var loading by remember { mutableStateOf(true) }

            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .shimmer(loading),
                onSuccess = {
                    loading = false
                },
                onError = {
                    Timber.d("The image was not loaded because of: ${it.result.throwable.message}")
                    loading = false
                },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.thumbnailUrl)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = album.title,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                Text(
                    text = album.title,
                    style = SparkTheme.typography.caption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChipTinted(
                        text = "Album #${album.albumId}"
                    )
                    ChipTinted(
                        text = "Track #${album.id}"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSparkApi::class)
@Composable
fun AlbumItemPlaceHolder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp),
    ) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .shimmer(true)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .shimmer(true),
            ) {
                Text(
                    text = "",
                    style = SparkTheme.typography.caption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChipTinted(
                        text = ""
                    )
                    ChipTinted(
                        text = ""
                    )
                }
            }
        }
    }
}
