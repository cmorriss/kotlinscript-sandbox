import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    kotlin("jvm") version "1.2.70"
}

repositories {
    jcenter()
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

val kotlinVersion="1.3.0-rc-80"

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    compile(kotlin("scripting-jvm-host", kotlinVersion))
    compile(kotlin("scripting-common", kotlinVersion))
    compile(kotlin("script-util", kotlinVersion))
}


kotlin {
    experimental.coroutines = Coroutines.ENABLE
}