import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidx.room)
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android {
    signingConfigs {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        create("release") {
            storeFile = file(keystoreProperties["storePath"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    namespace = "com.mk1morebugs.finapp"
    compileSdk = 34

    defaultConfig {
        applicationId =  "com.mk1morebugs.finapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("alpha") {
            versionNameSuffix = "-alpha"
            applicationIdSuffix = ".alpha"
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    // ui
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.compose.foundation)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // navigation-compose
    implementation(libs.navigation.compose)

    // coil
    implementation(libs.coil.compose)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    // room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)

    // datastore preferences
    implementation(libs.datastore.preferences)

    // lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.lifecycle.common.java8)
    testImplementation(libs.lifecycle.runtime.testing)

    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    testImplementation(libs.google.truth)
    androidTestImplementation(libs.google.truth)

    // MockK
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.agent)

    // Kotlinx Serialization Core
    implementation(libs.kotlinx.serialization.core)
}

kapt {
    correctErrorTypes = true
}
