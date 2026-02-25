package fr.leboncoin.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.cancellation.CancellationException

sealed interface LCResult<out T> {
    data class Success<T>(val data: T) : LCResult<T>
    data class Error(val exception: Throwable? = null) : LCResult<Nothing>
    data object Loading : LCResult<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<LCResult<T>> {
    return this
        .map<T, LCResult<T>> {
            LCResult.Success(it)
        }
        .onStart { emit(LCResult.Loading) }
        .catch {
            if (it is CancellationException) throw it
            emit(LCResult.Error(it))
        }
}

fun <T, R> LCResult<T>.mapSuccess(transform: (T) -> R): LCResult<R> {
    return when (this) {
        is LCResult.Success -> LCResult.Success(transform(data))
        is LCResult.Loading -> LCResult.Loading
        is LCResult.Error -> LCResult.Error(exception)
    }
}

fun <T> LCResult<T>.mapToUnitOnSuccess(): LCResult<Unit> {
    return when (this) {
        is LCResult.Success -> LCResult.Success(Unit)
        is LCResult.Loading -> LCResult.Loading
        is LCResult.Error -> LCResult.Error(exception)
    }
}

fun <T> LCResult<T>.mapOnError(transform: (Throwable?) -> Throwable?): LCResult<T> {
    return when (this) {
        is LCResult.Error -> LCResult.Error(transform(exception))
        else -> this@mapOnError
    }
}

suspend fun <T> LCResult<T>.onSuccess(block: suspend (T) -> Unit): LCResult<T>  {
    if (this is LCResult.Success) {
        block(data)
    }
    return this
}
