plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myandroidproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myandroidproject"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures{
        viewBinding = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.media3.common)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)

    androidTestImplementation(libs.espresso.core)

    //GET img from server
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    //noinspection UseTomlInstead
    implementation("com.airbnb.android:lottie:6.4.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
//    androidTestImplementation(libs.ui.test.junit4)
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
//    debugImplementation(libs.ui.tooling)
//    debugImplementation(libs.ui.test.manifest)

//    implementation ("com.squareup.okhttp:okhttp:2.7.2")
//    implementation ("com.squareup.retrofit2:retrofit:2.4.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.3.0")
}