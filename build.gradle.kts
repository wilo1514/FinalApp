// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        //classpath("com.google.dagger:hilt-android-gradle-plugin:2.46")
        classpath(libs.google.services)
    }
}

plugins {
    //id ("com.google.dagger.hilt.android") version "2.46" apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false

}