plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.gustavo.countries.domain"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
    implementation(libs.androidx.paging.common)

    testImplementation(libs.tests.mockk)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.kotlinx.coroutines.test)
}
