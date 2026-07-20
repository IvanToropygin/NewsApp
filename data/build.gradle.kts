import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

private val keystorePropertyFile = rootProject.file("keystore.properties")
private val keystoreProperties = keystorePropertyFile.inputStream().use { inputStream ->
    Properties().apply {
        load(inputStream)
    }
}
private val apiKey = keystoreProperties.getProperty("NEWS_API_KEY")

android {
    namespace = "com.example.data"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "NEWS_API_KEY", apiKey)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.appcompat)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.error.prone.annotations)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}