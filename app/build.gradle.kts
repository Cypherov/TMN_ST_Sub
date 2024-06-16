plugins {
    alias(libs.plugins.androidApplication)
    // firebase
    // google service gradle plugin
    id("com.google.gms.google-services")
    // firebase/
}

android {
    namespace = "com.example.monthly_apha"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.monthly_apha"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    //firebase
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    //Add the dependencies for Firebase products you want to use
    //When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    // for authentication
    implementation("com.google.firebase:firebase-auth")
    //for database
    implementation("com.google.firebase:firebase-database")
    //--firebase/
    //for hashing algos
    implementation("com.google.guava:guava:30.1-android")
    //
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.volley)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}