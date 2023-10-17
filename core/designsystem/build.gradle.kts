plugins {
    alias(libs.plugins.medicalassist.android.library)
    alias(libs.plugins.medicalassist.android.library.compose)
    alias(libs.plugins.medicalassist.android.library.jacoco)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.xela_dev_mobile.medical_assistant.core.designsystem"
}

dependencies {
    //lintPublish(projects.lint)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)

    debugApi(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt.compose)

    androidTestImplementation(projects.core.testing)
}
