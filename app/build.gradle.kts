plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.gustavo.countries"

    defaultConfig {
        applicationId = "dev.gustavo.countries"
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugaring)

    implementation(project(":feature:detail"))
    implementation(project(":feature:list"))
    implementation(project(":domain"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":data:repository"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.lifecycle)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.google.material)
    implementation(libs.bundles.hilt)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.test.androidx.compose.junit4)
    androidTestImplementation(libs.test.junit4)
    androidTestImplementation(libs.test.mockk)
    androidTestImplementation(libs.test.android.mockk)
}
