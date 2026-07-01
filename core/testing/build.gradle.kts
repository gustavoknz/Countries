plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.gustavo.countries.core.testing"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.paging.common)
    implementation(libs.test.mockk)
    implementation(libs.test.androidx.compose.junit4)

    androidTestImplementation(libs.bundles.instrumented.test)
}
