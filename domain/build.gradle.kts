import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.android.built-in-kotlin")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

configure<LibraryExtension> {
    namespace = "dev.gustavo.countries.domain"
    compileSdk = 37

    defaultConfig { minSdk = 23 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.hilt)
    kapt(libs.google.hilt.compiler)

    testImplementation(libs.tests.mockk)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.kotlinx.coroutines.test)
}
