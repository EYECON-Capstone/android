// settings.gradle.kts
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            // Core & UI versions
            version("androidxCore", "1.12.0")  // Menurunkan dari 1.15.0 ke 1.12.0
            version("appcompat", "1.6.1")
            version("material", "1.11.0")
            version("constraintlayout", "2.1.4")
            version("recyclerview", "1.3.2")
            version("cardview", "1.0.0")
            version("swiperefreshlayout", "1.1.0")

            // Lifecycle versions
            version("lifecycle", "2.6.2")
            version("activity", "1.8.2")

            // Navigation versions
            version("navigation", "2.7.6")

            // Camera versions
            version("camera", "1.3.1")

            // Network versions
            version("retrofit", "2.9.0")
            version("coroutines", "1.7.3")

            // Image loading
            version("glide", "4.16.0")

            // Auth versions
            version("playServices", "20.7.0")

            // Libraries definitions...
            library("androidx-core-ktx", "androidx.core", "core-ktx").versionRef("androidxCore")
            library("androidx-appcompat", "androidx.appcompat", "appcompat").versionRef("appcompat")
            library("material", "com.google.android.material", "material").versionRef("material")
            library("androidx-constraintlayout", "androidx.constraintlayout", "constraintlayout").versionRef("constraintlayout")
            library("androidx-recyclerview", "androidx.recyclerview", "recyclerview").versionRef("recyclerview")
            library("androidx-cardview", "androidx.cardview", "cardview").versionRef("cardview")
            library("androidx-swiperefreshlayout", "androidx.swiperefreshlayout", "swiperefreshlayout").versionRef("swiperefreshlayout")

            // Activity & Lifecycle
            library("androidx-activity", "androidx.activity", "activity").versionRef("activity")
            library("androidx-lifecycle-livedata-ktx", "androidx.lifecycle", "lifecycle-livedata-ktx").versionRef("lifecycle")
            library("androidx-lifecycle-viewmodel-ktx", "androidx.lifecycle", "lifecycle-viewmodel-ktx").versionRef("lifecycle")
            library("androidx-lifecycle-runtime-ktx", "androidx.lifecycle", "lifecycle-runtime-ktx").versionRef("lifecycle")

            // Navigation
            library("androidx-navigation-fragment-ktx", "androidx.navigation", "navigation-fragment-ktx").versionRef("navigation")
            library("androidx-navigation-ui-ktx", "androidx.navigation", "navigation-ui-ktx").versionRef("navigation")

            // Camera
            library("androidx-camera-camera2", "androidx.camera", "camera-camera2").versionRef("camera")
            library("androidx-camera-lifecycle", "androidx.camera", "camera-lifecycle").versionRef("camera")
            library("androidx-camera-view", "androidx.camera", "camera-view").versionRef("camera")

            // Auth
            library("firebase-auth", "com.google.firebase", "firebase-auth-ktx")
            library("google-play-services-auth", "com.google.android.gms", "play-services-auth").versionRef("playServices")

            // Network
            library("retrofit", "com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            library("retrofit2-converter-gson", "com.squareup.retrofit2", "converter-gson").versionRef("retrofit")
            library("coroutines-android", "org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef("coroutines")

            // Image Loading
            library("glide", "com.github.bumptech.glide", "glide").versionRef("glide")

            // Gemini AI
            library("generativeai", "com.google.ai.client.generativeai", "generativeai").version("0.1.1")

            // Testing
            library("junit", "junit", "junit").version("4.13.2")
            library("androidx-junit", "androidx.test.ext", "junit").version("1.1.5")
            library("androidx-espresso-core", "androidx.test.espresso", "espresso-core").version("3.5.1")
        }
    }
}

rootProject.name = "EYECON"
include(":app")