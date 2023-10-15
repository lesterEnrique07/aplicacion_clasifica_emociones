package com.xela_dev_mobile.medical_assistant.core.analytics

/**
 * Implementation of AnalyticsHelper which does nothing. Useful for tests and previews.
 */
class NoOpAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) = Unit
}
