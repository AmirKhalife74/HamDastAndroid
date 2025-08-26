pluginManagement {
    repositories {
        maven("https://maven.myket.ir")
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.myket.ir")
        maven("https://jitpack.io")
    }
}

rootProject.name = "HamDast"
include(":app")
