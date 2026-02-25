package fr.leboncoin.ui.pagingdsl

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

@DslMarker
annotation class PagingDSL

@PagingDSL
class PagingHandlerScope<T : Any>(
    private val stableItems: StablePagingItems<T>
) {
    private var handled = false
    private val loadState = derivedStateOf { stableItems.items.loadState }.value

    @SuppressLint("ComposableNaming")
    @Composable
    fun onEmpty(body: @Composable () -> Unit) {
        if (handled) return
        if (loadState.refresh !is LoadState.Error && stableItems.items.itemCount == 0) {
            handled = true
            body()
        }
    }

    @SuppressLint("ComposableNaming")
    @Composable
    fun onRefresh(body: @Composable () -> Unit) {
        if (handled) return
        if (loadState.refresh is LoadState.Loading && stableItems.items.itemCount == 0) {
            handled = true
            body()
        }
    }

    @SuppressLint("ComposableNaming")
    @Composable
    fun onSuccess(body: @Composable (StablePagingItems<T>) -> Unit) {
        if (!handled) {
            handled = true
            body(stableItems)
        }
    }

    @SuppressLint("ComposableNaming")
    @Composable
    fun onError(body: @Composable (Throwable) -> Unit) {
        if (handled) return
        if (loadState.refresh is LoadState.Error) {
            val error = (loadState.refresh as LoadState.Error).error
            handled = true
            body(error)
        }
    }

    @LazyScopeMarker
    fun LazyListScope.onAppendItem(body: @Composable LazyItemScope.() -> Unit) {
        if (loadState.append == LoadState.Loading) {
            item { body(this) }
        }
    }

    @LazyScopeMarker
    fun LazyListScope.onLastItem(body: @Composable LazyItemScope.() -> Unit) {
        if (loadState.append.endOfPaginationReached) item { body(this) }
    }

    @LazyScopeMarker
    fun LazyListScope.onPagingItems(key: ((T) -> Any)?, body: @Composable LazyItemScope.(index: Int, item: T) -> Unit) {
        items(
            count = stableItems.items.itemCount,
            key = stableItems.items.itemKey(key),
        ) { index ->
            val item = stableItems.items[index]
            item?.let {
                body(index, it)
            }
        }
    }
}

@Composable
fun <T : Any> HandlePagingItems(
    items: StablePagingItems<T>,
    content: @Composable PagingHandlerScope<T>.() -> Unit
) {
    PagingHandlerScope(items).content()
}

@Immutable
class StablePagingItems<T : Any>(
    val items: LazyPagingItems<T>
)