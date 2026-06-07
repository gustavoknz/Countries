import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.android.built-in-kotlin")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

configure<LibraryExtension> {
    namespace = "dev.gustavo.countries.feature.list"
    compileSdk = 37

    defaultConfig { minSdk = 23 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures { compose = true }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coil.compose)
    implementation(libs.google.hilt)
    implementation(libs.google.hilt.navigationcompose)
    kapt(libs.google.hilt.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.immutablecollections)

    testImplementation(libs.tests.junit5)
    testImplementation(libs.tests.mockk)
    testImplementation(libs.tests.turbine)
    testImplementation(libs.tests.kotlinx.coroutines.test)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.androidx.archcoretesting)

    debugImplementation(libs.compose.ui.tooling)
}
