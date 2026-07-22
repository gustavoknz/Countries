plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "dev.gustavo.countries.detekt"

dependencies {
    implementation(libs.detekt.api)
}
