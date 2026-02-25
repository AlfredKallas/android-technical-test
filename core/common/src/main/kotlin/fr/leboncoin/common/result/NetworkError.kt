package fr.leboncoin.common.result

import java.lang.Exception

/**
 * A sealed class representing various network-related errors.
 * This can be expanded to include custom backend exceptions.
 */
sealed class NetworkError : Exception() {
    data class HttpError(val code: Int, override val message: String) : NetworkError()
    data class NetworkException(val source: Throwable) : NetworkError()
    data class UnknownError(val source: Throwable? = null) : NetworkError()
    
}
