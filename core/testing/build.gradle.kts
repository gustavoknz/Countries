plugins {
    id("countries.android.library")
    id("countries.android.compose")
    id("countries.android.hilt")
    id("countries.jacoco")
}

android {
    namespace = "dev.gustavo.countries.core.testing"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.paging.common)
    implementation(libs.test.mockk)
    implementation(libs.test.androidx.compose.junit4)

    androidTestImplementation(libs.bundles.instrumented.test)
}
