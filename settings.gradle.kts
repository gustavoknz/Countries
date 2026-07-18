pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Countries"

include(":app")
include(":feature:list")
include(":feature:detail")
include(":domain")
include(":data:remote")
include(":data:local")
include(":data:repository")
include(":core:ui")
include(":core:common")
include(":core:testing")
include(":core:detekt-rules")
