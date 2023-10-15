
plugins {
    alias(libs.plugins.medicalassist.android.library)
    alias(libs.plugins.medicalassist.android.library.compose)
    alias(libs.plugins.medicalassist.android.hilt)
}

android {
    namespace = "com.xela_dev_mobile.medical_assistant.core.testing"
}

dependencies {
    api(libs.accompanist.testharness)
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.core)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.junit4)
    api(libs.kotlinx.coroutines.test)
    api(libs.roborazzi)
    api(libs.robolectric.shadows)
    api(libs.turbine)

    debugApi(libs.androidx.compose.ui.testManifest)

    implementation(projects.core.analytics)
    implementation(projects.core.designsystem)

    /*implementation(projects.core.common)
    implementation(projects.core.data)

    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.notifications)*/
    implementation(libs.kotlinx.datetime)
}
