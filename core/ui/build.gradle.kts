plugins {
    id("countries.android.library")
    id("countries.android.compose")
    id("countries.jacoco")
    id("countries.roborazzi")
}

android {
    namespace = "dev.gustavo.countries.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.coil.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(project(":core:testing"))
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.test.androidx.compose.junit4)
    testImplementation(libs.test.androidx.test.runner)
}
