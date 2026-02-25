package fr.leboncoin.common.result

import java.lang.Exception

/**
 * A sealed class representing functional, business-logic errors.
 * These are distinct from infrastructure/network errors.
 */
sealed class DomainError : Exception() {
    data class NotFound(val id: Long) : DomainError()
}
