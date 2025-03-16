plugins {
    alias(libs.plugins.android.application) // Hoặc dùng id("com.android.application") nhưng chỉ chọn 1
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    kotlin("kapt")
    alias(libs.plugins.hilt) // Nếu đã khai báo Hilt trong libs.versions.toml
}


android {
    namespace = "com.example.clonetiktok"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.clonetiktok"
        minSdk = 34
        targetSdk = 34
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.compose.material:material:1.4.1")

// ComponentActivity
    implementation("androidx.activity:activity-compose:1.7.0")

    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui")

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")

    implementation("io.coil-kt:coil-compose:2.1.0")

    implementation("androidx.media3:media3-exoplayer:1.0.0")
    implementation("androidx.media3:media3-exoplayer-dash:1.0.0")

    implementation("androidx.media3:media3-ui:1.0.0")

    implementation("androidx.compose.animation:animation:1.6.1")
    implementation("androidx.compose.material:material-icons-extended:1.5.1")

    implementation("com.google.accompanist:accompanist-pager:0.31.1-alpha")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
