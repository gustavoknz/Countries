plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.gustavo.countries.data.repository"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(project(":core:common"))

    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)

    testImplementation(libs.tests.junit5)
    testImplementation(libs.tests.mockk)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.kotlinx.coroutines.test)
}
