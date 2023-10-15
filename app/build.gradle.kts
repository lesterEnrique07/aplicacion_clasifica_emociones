plugins {
    alias(libs.plugins.medicalassist.android.application)
    alias(libs.plugins.medicalassist.android.application.compose)
    alias(libs.plugins.medicalassist.android.application.flavors)
    alias(libs.plugins.medicalassist.android.application.jacoco)
    alias(libs.plugins.medicalassist.android.hilt)
    id("jacoco")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "com.xela_dev_mobile.medical_assistant"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.xela_dev_mobile.medical_assistant"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(projects.core.ui)
    implementation(projects.core.designsystem)

    /*implementation(projects.feature.interests)
    implementation(projects.feature.foryou)
    implementation(projects.feature.bookmarks)
    implementation(projects.feature.topic)
    implementation(projects.feature.search)
    implementation(projects.feature.settings)*/

    /*implementation(projects.core.common)

    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.analytics)

    implementation(projects.sync.work)


    androidTestImplementation(projects.core.datastoreTest)
    androidTestImplementation(projects.core.dataTest)
    androidTestImplementation(projects.core.network)*/
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.accompanist.testharness)
    androidTestImplementation(kotlin("test"))
    debugImplementation(libs.androidx.compose.ui.testManifest)
    //debugImplementation(projects.uiTestHiltManifest)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.window.manager)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)

    // Core functions
    testImplementation(projects.core.testing)
    /*testImplementation(projects.core.datastoreTest)
    testImplementation(projects.core.dataTest)
    testImplementation(projects.core.network)*/
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.accompanist.testharness)
    testImplementation(libs.work.testing)
    testImplementation(kotlin("test"))
    kaptTest(libs.hilt.compiler)
}