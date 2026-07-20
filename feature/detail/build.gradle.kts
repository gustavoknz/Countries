plugins {
    id("countries.android.library")
    id("countries.android.compose")
    id("countries.android.hilt")
    id("countries.jacoco")
    id("countries.roborazzi")
}

android {
    namespace = "dev.gustavo.countries.feature.detail"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.test.androidx.arch.core)

    debugImplementation(libs.bundles.compose.debug)

    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(libs.test.junit4)
    androidTestImplementation(libs.test.android.mockk)
}
