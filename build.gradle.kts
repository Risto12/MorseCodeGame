buildscript {
    extra.apply {
        set("compose_version", "1.2.0")
        set("kotlin_version", "1.7.0")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}