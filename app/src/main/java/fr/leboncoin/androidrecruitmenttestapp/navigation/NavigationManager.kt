package fr.leboncoin.androidrecruitmenttestapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    private val _navigationEvents = MutableSharedFlow<MainDestination>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun navigateTo(destination: MainDestination) {
        _navigationEvents.tryEmit(destination)
    }

    @OptIn(FlowPreview::class)
    @Composable
    fun HandleNavigationEvents(onNavigate: (MainDestination) -> Unit) {
        val events = remember { _navigationEvents }
        androidx.compose.runtime.LaunchedEffect(events) {
            events
                .debounce(500L) // Prevent multiple rapid clicks
                .onEach { onNavigate(it) }
                .launchIn(this)
        }
    }
}
