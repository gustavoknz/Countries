plugins {
    id("countries.android.library")
    id("countries.android.compose")
    id("countries.android.hilt")
    id("countries.jacoco")
    id("countries.roborazzi")
}

android {
    namespace = "dev.gustavo.countries.feature.list"

    kotlin {
        compilerOptions {
            optIn.add("androidx.paging.ExperimentalPagingApi")
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.paging)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.collections.immutable)
    debugImplementation(libs.bundles.compose.debug)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.test.androidx.arch.core)

    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(libs.test.mockk)
    androidTestImplementation(libs.test.android.mockk)
}
