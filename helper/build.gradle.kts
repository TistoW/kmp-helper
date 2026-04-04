import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "Helper"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
//            implementation(project(":helper:common"))
//            implementation(project(":helper:helper"))

            // Ktor
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Kotlinx
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            // Koin
            implementation(libs.koin.core)

            // Helper
            implementation(libs.uuid)
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.darwin)
        }

        jvmMain.dependencies {
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.cio)
        }

        jsMain.dependencies {
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.js)
        }

        wasmJsMain.dependencies {
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.js)
        }
    }
}

android {
    namespace = "com.tisto.kmp.helper"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
