package fr.leboncoin.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.compose.ui.res.painterResource
import com.adevinta.spark.components.image.Illustration
import fr.leboncoin.resources.R

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.AlbumDetailsEntry() {
    entry<AlbumDetailsNavKey>(
        metadata = ListDetailSceneStrategy.detailPane(),
    ) { key ->
        Illustration(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.work_in_progress),
            contentDescription = null,
            contentScale = ContentScale.Inside,
        )
    }
}