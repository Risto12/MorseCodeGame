plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
    id("org.jlleitschuh.gradle.ktlint") version "11.2.0"
    id("com.google.dagger.hilt.android")
}

ktlint {
    android.set(true)
}

val composeVersion = rootProject.extra["compose_version"]

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.galaxy.morsecodegame"
        minSdk = 31
        //noinspection OldTargetApi
        targetSdk = 31 // Tested with real device that had version 31
        versionCode = 1
        versionName = "1.2"

        testInstrumentationRunner = "com.galaxy.morsecodegame.MorseCodeGameTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            buildConfigField("Boolean", "DEBUG_MODE", "true")
        }
        release {
            buildConfigField("Boolean", "DEBUG_MODE", "false")
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion as String?
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    val roomVersion = "2.4.3"
    val hiltVersion = "2.44"
    val material3Version = "1.0.1"
    // TODO add comments why obsolete dependency is used
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    //noinspection GradleDependency,GradleDependency
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-rxjava2:$roomVersion")
    implementation("androidx.room:room-rxjava3:$roomVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.navigation:navigation-compose:2.5.1")
    implementation("androidx.compose.material3:material3:$material3Version")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //noinspection GradleDependency
    implementation("androidx.room:room-guava:$roomVersion")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
}

