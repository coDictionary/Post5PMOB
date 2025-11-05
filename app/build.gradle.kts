plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.farhan164.post5pmob"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.farhan164.post5pmob"
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

    buildFeatures {
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

    dependencies {
        // Tag ini catatan yee, kali aja lupa kan

        // Room Database
        val room_version = "2.6.1"
        implementation ("androidx.room:room-runtime:$room_version")
        implementation ("androidx.room:room-ktx:$room_version") // Untuk Coroutines (suspend function)
        kapt ("androidx.room:room-compiler:$room_version")

        // Lifecycle (ViewModel & LiveData)
        val lifecycle_version = "2.8.3"
        implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
        implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

        // Activity KTX untuk by ViewModels
        val activity_version = "1.9.0"
        implementation ("androidx.activity:activity-ktx:$activity_version")

        // RecyclerView & Material Design
        implementation ("androidx.recyclerview:recyclerview:1.3.2")
        implementation ("com.google.android.material:material:1.12.0")

        // Coroutines
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

        // Glide (Untuk memuat gambar dari URI/Galeri)
        val glide_version = "4.16.0"
        implementation ("com.github.bumptech.glide:glide:$glide_version")
        kapt ("com.github.bumptech.glide:compiler:$glide_version")
    }
}