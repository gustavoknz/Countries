plugins {
    id("countries.android.library")
    id("countries.android.compose")
    id("countries.android.hilt")
    id("countries.jacoco")
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

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.test.androidx.compose.manifest)

    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.test.androidx.compose.junit4)
    androidTestImplementation(libs.test.junit4)
    androidTestImplementation(libs.test.mockk)
    androidTestImplementation(libs.test.android.mockk)
}
