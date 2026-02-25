package fr.leboncoin.ui.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import com.adevinta.spark.components.snackbars.SnackbarHostState

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}
