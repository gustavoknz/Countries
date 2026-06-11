plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.gustavo.countries.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit)
    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
}
