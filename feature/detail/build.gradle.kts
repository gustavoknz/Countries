plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.gustavo.countries.feature.detail"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.google.hilt)
    implementation(libs.google.hilt.navigationcompose)
    ksp(libs.google.hilt.compiler)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.tests.androidx.archcoretesting)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.junit5)
    testImplementation(libs.tests.kotlinx.coroutines.test)
    testImplementation(libs.tests.mockk)
    testImplementation(libs.tests.turbine)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.testing.manifest)

    androidTestImplementation(libs.androidx.compose.testing.junit4)
    androidTestImplementation(libs.tests.junit4)
}
