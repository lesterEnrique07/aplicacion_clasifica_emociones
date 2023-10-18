package com.xeladevmobile.medicalassistant

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class MedicalBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
    BENCHMARK(".benchmark")
}
