import com.android.build.gradle.internal.packaging.defaultExcludes

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.flicker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.flicker"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    //Firebase
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    //Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.7")
    //Material icons
    implementation("androidx.compose.material:material-icons-extended:1.7.4")
    // SplashScreen
    implementation ("androidx.core:core-splashscreen:1.0.1")
    //koin
    val koinVersion = "3.5.6"
    implementation(platform("io.insert-koin:koin-bom:$koinVersion"))
    // Koin for Core
    implementation("io.insert-koin:koin-core")
    // Koin for Android
    implementation("io.insert-koin:koin-android")
    // Koin for Jetpack Compose
    implementation("io.insert-koin:koin-androidx-compose")
    //Coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    // Jetpack Media3 - Player (ExoPlayer)
    implementation ("androidx.media3:media3-exoplayer:1.7.1")
    // Jetpack Media3 - UI (PlayerView, StyledPlayerView)
    // Para los controles y la vista del reproductor
    implementation ("androidx.media3:media3-ui:1.7.1")
    // Jetpack Media3 - HLS
    implementation ("androidx.media3:media3-exoplayer-hls:1.7.1")
    // Gson para SharedPreferences
    implementation("com.google.code.gson:gson:2.10.1")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}