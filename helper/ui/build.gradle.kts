import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlin.compose)
}

// ============================================
// READ local.properties
// ============================================
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

val kmpTargetsEnabled = localProperties.getProperty("kmp.targets.enabled", "true").toBoolean()


kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    if (kmpTargetsEnabled) {
        // iOS
        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                baseName = "helper-ui"
                isStatic = true
            }
        }

        // JVM (Desktop)
        jvm()

        // JavaScript
        js {
            browser()
        }

        // WebAssembly
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
        }
    }

    sourceSets {
        commonMain.dependencies {

            implementation(project(":helper:network"))
            implementation(project(":helper:utils"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)

            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)

            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.lifecycle.viewmodelCompose)
            implementation(libs.lifecycle.runtimeCompose)

            implementation(libs.coil) // Core Coil
            implementation(libs.coil3.coil.compose) // AsyncImage composable
            implementation(libs.coil.network.ktor3)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

        }

        androidMain.dependencies {

        }

        iosMain.dependencies {

        }

        jvmMain.dependencies {

        }

        jsMain.dependencies {

        }

        wasmJsMain.dependencies {

        }
    }
}

android {
    namespace = "com.tisto.kmp.helper.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
