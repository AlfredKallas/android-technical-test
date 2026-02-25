package fr.leboncoin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import fr.leboncoin.ui.util.TestTags

@Composable
fun AlbumsLoadingScreen() {
    LazyColumn(
        modifier = Modifier.testTag(TestTags.LOADING_SCREEN),
        userScrollEnabled = false,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(10) {
            AlbumItemPlaceHolder()
        }
    }
}