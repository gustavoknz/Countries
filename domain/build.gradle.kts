plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.gustavo.countries.domain"

    kotlin {
        compilerOptions {
            optIn.add("androidx.paging.ExperimentalPagingApi")
        }
    }
}

dependencies {
    implementation(libs.androidx.paging.common)
    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.kotlinx.coroutines.test)
    testImplementation(libs.tests.mockk)
}
