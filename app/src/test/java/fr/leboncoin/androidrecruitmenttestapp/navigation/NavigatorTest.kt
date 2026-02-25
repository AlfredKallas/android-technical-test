package fr.leboncoin.androidrecruitmenttestapp.navigation

import androidx.navigation3.runtime.NavBackStack
import fr.leboncoin.ui.navigation.AlbumDetailsNavKey
import fr.leboncoin.ui.navigation.AlbumsNavKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class NavigatorTest {

    private fun createNavigator(initialKey: AlbumsNavKey = AlbumsNavKey) = Navigator(
        currentBackStack = NavBackStack(initialKey)
    )

    @Test
    fun `GIVEN start destination WHEN navigate THEN should have a new destination`() {
        // GIVEN
        val navigator = createNavigator()
        assertEquals(AlbumsNavKey, navigator.currentKey)

        // WHEN
        navigator.navigate(AlbumDetailsNavKey(1L))

        // THEN
        assertNotEquals(AlbumsNavKey, navigator.currentKey)
        assertEquals(AlbumDetailsNavKey(1L), navigator.currentKey)
    }

    @Test
    fun `GIVEN a destination WHEN goBack THEN should be back to start destination`() {
        // GIVEN
        val navigator = createNavigator()
        navigator.navigate(AlbumDetailsNavKey(1L))
        assertEquals(AlbumDetailsNavKey(1L), navigator.currentKey)

        // WHEN
        navigator.goBack()

        // THEN
        assertEquals(AlbumsNavKey, navigator.currentKey)
    }

    @Test(expected = IllegalStateException::class)
    fun `GIVEN start destination WHEN goBack THEN should throw an exception`() {
        // GIVEN
        val navigator = createNavigator()
        assertEquals(AlbumsNavKey, navigator.currentKey)

        // WHEN
        navigator.goBack()
    }
}
