package fr.leboncoin.androidrecruitmenttestapp.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import fr.leboncoin.ui.navigation.AlbumsNavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 *
 * @param state - The navigation state that will be updated in response to navigation events.
 */
class Navigator(private val currentBackStack : NavBackStack<NavKey>) {

    val currentKey: NavKey by derivedStateOf { currentBackStack.last() }

    /**
     * Navigate to a navigation key
     *
     * @param key - the navigation key to navigate to.
     */
    fun navigate(key: NavKey) {
        if (currentBackStack.last() != key) {
            goToKey(key)
        }
    }

    /**
     * Go back to the previous navigation key.
     */
    fun goBack() {
        when (currentKey) {
            AlbumsNavKey -> error("You cannot go back from the start route")
            else -> currentBackStack.removeLastOrNull()
        }
    }

    /**
     * Go to a key.
     */
    private fun goToKey(key: NavKey) {
        currentBackStack.apply {
            // Remove it if it's already in the stack so it's added at the end.
            remove(key)
            add(key)
        }
    }

    /**
     * Clearing all but the root key in the current sub stack.
     */
    private fun clearSubStack() {
        currentBackStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}