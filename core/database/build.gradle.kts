plugins {
    alias(libs.plugins.medicalassist.android.library)
    alias(libs.plugins.medicalassist.android.library.jacoco)
    alias(libs.plugins.medicalassist.android.hilt)
    alias(libs.plugins.medicalassist.android.room)
}

android {
    defaultConfig {
        testInstrumentationRunner =
            "com.xeladevmobile.medicalassistant.core.testing.MedicalTestRunner"
    }
    namespace = "com.xelamobiledev.core.database"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(projects.core.testing)
}