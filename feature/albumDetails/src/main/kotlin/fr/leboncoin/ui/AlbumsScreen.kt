package fr.leboncoin.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.adevinta.spark.components.progress.CircularProgressIndicator
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.ui.pagingdsl.HandlePagingItems
import fr.leboncoin.ui.pagingdsl.StablePagingItems
import fr.leboncoin.data.model.Album

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    onItemSelected : (Album) -> Unit,
) {
    val viewModel: AlbumDetailsViewModel = hiltViewModel()
    val pagingItems = viewModel.paginationFlow.collectAsLazyPagingItems()

    val stableItems = remember(pagingItems) { StablePagingItems(pagingItems) }

    AlbumsScreen(
        modifier = modifier,
        stablePagingItems = stableItems,
        onItemSelected = onItemSelected
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    stablePagingItems: StablePagingItems<Album>,
    onItemSelected : (Album) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            val dpCacheWindow = LazyLayoutCacheWindow(ahead = 150.dp, behind = 150.dp)

            val songsListState = rememberLazyListState(
                cacheWindow = dpCacheWindow
            )

            HandlePagingItems(stablePagingItems) {
                onRefresh { SongsLoadingScreen() }
                onError { error ->
                    ErrorScreen(
                        message = error.message.orEmpty(),
                        onRetry = { stablePagingItems.items.retry() }
                    )
                }
                onSuccess { _ ->
                    LazyColumn(
                        state = songsListState,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        onPagingItems(key = { it.id }) { index, song ->
                            AlbumItem(
                                album = song,
                                onItemSelected = onItemSelected,
                            )
                        }
                        onAppendItem { CircularProgressIndicator(progress = { 1f }, Modifier.padding(6.dp)) }
                    }
                }
            }
        }
    }
}


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun SongsLoadingScreen() {
    LazyColumn(
        userScrollEnabled = false,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(10) {
            PlaceHolderSongCard()
        }
    }
}

@Composable
fun PlaceHolderSongCard(
) {
    Card {
        Row(
            Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .size(150.pxToDp())
                .clip(RoundedCornerShape(12.dp))
                .shimmer(true)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(true),
                text = "",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
            )
        }
    }
}

@Suppress("AssignedValueIsNeverRead")
@Composable
private fun Modifier.shimmer(
    cornerRadius: Dp = 0.dp,
    durationMillis: Int = 1000
): Modifier = composed {
    var elementSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 1.0f),
        Color.LightGray.copy(alpha = 0.2f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = -2 * elementSize.width.toFloat(),
        targetValue = 2 * elementSize.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis, // slower = smoother
                easing = FastOutSlowInEasing // smoother easing
            )
        )
    )

    this.drawWithCache {
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim, 0f),
            // wider gradient
            end = Offset(translateAnim + size.width, size.height)
        )
        val cornerPx = cornerRadius.toPx()
        onDrawBehind {
            drawRoundRect(
                brush = brush,
                cornerRadius = CornerRadius(cornerPx, cornerPx),
                size = size
            )
        }
    }.onGloballyPositioned{
        elementSize = it.size
    }
}

@Composable
fun Modifier.shimmer(
    isLoading: Boolean,
    cornerRadius: Dp = 0.dp,
    durationMillis: Int = 2000
): Modifier {
    return if (isLoading)
        this.shimmer(
            cornerRadius = cornerRadius,
            durationMillis = durationMillis)
    else this
}

@Composable
fun ErrorScreen(
    title: String = "Retry",
    message: String,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $message"
        )
        Button(
            onClick = onRetry
        ){
            Text(title)
        }
    }
}