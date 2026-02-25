package fr.leboncoin.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun ErrorScreen(
    title: String? = null,
    message: String,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .testTag(TestTags.ERROR_SCREEN)
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.error_prefix, message)
        )
        ButtonFilled(
            onClick = onRetry
        ){
            Text(title ?: stringResource(R.string.retry_button))
        }
    }
}
