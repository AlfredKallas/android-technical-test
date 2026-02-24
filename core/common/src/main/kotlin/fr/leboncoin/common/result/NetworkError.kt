package fr.leboncoin.common.result

/**
 * A sealed class representing various network-related errors.
 * This can be expanded to include custom backend exceptions.
 */
sealed class NetworkError : Throwable() {
    data class HttpError(val code: Int, override val message: String) : NetworkError()
    data class NetworkException(val source: Throwable) : NetworkError()
    data class SerializationError(val source: Throwable) : NetworkError()
    data class UnknownError(val source: Throwable? = null) : NetworkError()
    
    // Example of a custom backend exception
    data class BackendError(val errorCode: String, override val message: String) : NetworkError()
}
