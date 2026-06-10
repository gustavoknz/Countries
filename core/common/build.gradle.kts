plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.gustavo.countries.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit)
    implementation("javax.inject:javax.inject:1")
}
