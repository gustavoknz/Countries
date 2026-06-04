pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

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
