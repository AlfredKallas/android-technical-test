package fr.leboncoin.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.text.Text
import fr.leboncoin.resources.R
import androidx.compose.ui.platform.testTag
import fr.leboncoin.ui.util.TestTags

@Composable
fun EmptyScreen(text: String, onRetry: (() -> Unit)? = null) {
    Column(modifier = Modifier
        .testTag(TestTags.EMPTY_SCREEN)
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.size(16.dp))
        if (onRetry != null) {
            ButtonFilled(onClick = onRetry) {
                Text(stringResource(R.string.refresh_button))
            }
        }
    }
}