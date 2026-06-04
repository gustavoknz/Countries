plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "dev.gustavo.countries"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.gustavo.countries"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugaringjdklibs)

    implementation(project(":feature:list"))
    implementation(project(":feature:detail"))
    implementation(project(":domain"))
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(project(":data:repository"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.google.material)
    implementation(libs.compose.activity)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.google.hilt)
    implementation(libs.google.hilt.navigationcompose)
    kapt(libs.google.hilt.compiler)

    debugImplementation(libs.compose.ui.tooling)
}
