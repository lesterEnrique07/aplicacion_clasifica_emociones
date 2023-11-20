plugins {
    alias(libs.plugins.medicalassist.android.feature)
    alias(libs.plugins.medicalassist.android.library.compose)
    alias(libs.plugins.medicalassist.android.library.jacoco)
}

android {
    namespace = "com.xeladevmobile.medicalassistant.feature.login"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.network)

    implementation(libs.androidx.compose.material3.windowSizeClass)

    //implementation(libs.accompanist.flowlayout)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.activity.compose)
}