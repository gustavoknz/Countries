import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.android.library)
    id("com.android.built-in-kotlin")
}

configure<LibraryExtension> {
    namespace = "dev.gustavo.countries.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit)
    // @Inject annotation — javax.inject is bundled with hilt/dagger but
    // we only need the annotation here, so we use the standalone artifact.
    implementation("javax.inject:javax.inject:1")
}
