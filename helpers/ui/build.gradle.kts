import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
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

val kmpTargetsEnabled = localProperties.getProperty("kmp.targets.enabled", "false").toBoolean()


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

            implementation(project(":helpers:network"))
            implementation(project(":helpers:utils"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.lifecycle.viewmodelCompose)
            implementation(libs.lifecycle.runtimeCompose)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)

            // Ktor
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            // ktor socket
            implementation(libs.ktor.client.websockets)

            // Kotlinx
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Helper
            implementation(libs.uuid)
            implementation(libs.kotlinx.datetime)


            implementation(libs.filekit.dialogs.compose)
            implementation(libs.filekit.core)

            implementation(libs.coil) // Core Coil
            implementation(libs.coil3.coil.compose) // AsyncImage composable
            implementation(libs.coil.network.ktor3)
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.activity.compose)

            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.view)
            implementation(libs.barcode.scanning)
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

dependencies {
    // Fix: Using only the Jetbrains version of ui-tooling to avoid ClassNotFoundException in Previews
    // as the project primarily uses Compose Multiplatform dependencies.
    debugImplementation(libs.compose.uiTooling)
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
