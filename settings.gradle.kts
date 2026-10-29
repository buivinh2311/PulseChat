pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ChatApplication"
include(":app")
include(":core-resource")
include(":core-ui")
include(":feature-conversation")
include(":share-presentation")
include(":feature-settings")
include(":feature-contacts")
include(":feature-auth")
include(":core-model")
include(":infrastructure")
include(":core-domain")
include(":core-network")
include(":core-utils")
include(":feature-search")
include(":feature-profile")
