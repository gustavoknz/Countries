import com.android.build.api.dsl.LibraryExtension

plugins {
    id("countries.android.library")
    id("countries.android.hilt")
    id("countries.jacoco")
    alias(libs.plugins.kotlin.serialization)
}

configure<LibraryExtension> {
    namespace = "dev.gustavo.countries.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.retrofit)

    testImplementation(libs.bundles.unit.test)

    androidTestImplementation(libs.bundles.instrumented.test)
}
