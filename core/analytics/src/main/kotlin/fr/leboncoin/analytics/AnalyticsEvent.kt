package fr.leboncoin.analytics

import kotlinx.serialization.Serializable

interface AnalyticsCategory {
    val name: String
    val analyticsAtScreen: String
}

/**
 * Represents an analytics event.
 *
 * @param type - the event type. Wherever possible use one of the standard
 * event `Types`, however, if there is no suitable event type already defined, a custom event can be
 * defined as long as it is configured in your backend analytics system (for example, by creating a
 * Firebase Analytics custom event).
 *
 * @param extras - list of parameters which supply additional context to the event. See `Param`.
 */
data class AnalyticsEvent(
    val type: AnalyticsCategory,
    val extras: List<Param> = emptyList(),
) {
    // Standard analytics types.
    sealed class AnalyticsType(override val analyticsAtScreen: String, override val name: String): AnalyticsCategory {
        data class ScreenView(override val analyticsAtScreen: String) : AnalyticsType(analyticsAtScreen,"screen_view")
        data class UserInteraction(override val analyticsAtScreen: String) : AnalyticsType(analyticsAtScreen,"user_interaction")
    }

    /**
     * A key-value pair used to supply extra context to an analytics event.
     *
     * @param key - the parameter key. Wherever possible use one of the standard `ParamKeys`,
     * however, if no suitable key is available you can define your own as long as it is configured
     * in your backend analytics system (for example, by creating a Firebase Analytics custom
     * parameter).
     *
     * @param value - the parameter value.
     */
    @Serializable
    data class Param(val key: String, val value: String)
}
