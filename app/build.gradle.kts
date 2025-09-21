plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // Navigation Component
    alias(libs.plugins.navigation.safeargs)
    // Kotlin Kapt for Moshi
    alias(libs.plugins.kotlin.kapt)
    // Dagger Hilt
    alias(libs.plugins.dagger.hilt)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.hamdast"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.hamdast"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)


    kapt(libs.androidx.room.compiler)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.livedata.core.ktx)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidxTransition)

    // Navigation Compnenet
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp.logging)
    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    kapt(libs.moshi.kotlin.codegen)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)


    // Lotie
    implementation(libs.android.lottie)

    implementation ("me.relex:circleindicator:2.1.6")
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    //Worker
    implementation ("androidx.work:work-runtime-ktx:2.10.4")

    //data and time
    implementation ("com.github.samanzamani:PersianDate:1.7.1")
    //Jalali Date Picker
    implementation ("com.github.aliab:Persian-Date-Picker-Dialog:1.8.0")

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
}