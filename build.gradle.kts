// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    //Firebase
    id("com.google.gms.google-services") version "4.4.1" apply false

    id("com.google.dagger.hilt.android") version "2.44" apply false

    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false

}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }

}

