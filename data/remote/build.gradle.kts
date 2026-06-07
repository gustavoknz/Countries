import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.android.built-in-kotlin")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

configure<LibraryExtension> {
    namespace = "dev.gustavo.countries.data.remote"
    compileSdk = 37

    defaultConfig { minSdk = 23 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:common"))

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logginginterceptor)
    implementation(libs.google.gson)
    implementation(libs.google.hilt)
    kapt(libs.google.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.tests.junit5)
    testImplementation(libs.tests.mockk)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.kotlinx.coroutines.test)
}
