import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import kotlin.apply

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
                baseName = "helper-android"
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

            implementation(project(":helper:utils"))
            implementation(project(":helper:network"))

            implementation(libs.compose.runtime)
            implementation(libs.lifecycle.viewmodelCompose)
            implementation(libs.lifecycle.runtimeCompose)

            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs.compose)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.material)

            implementation(libs.converter.gson)
            implementation(libs.gson)

            implementation(libs.zxing.android.embedded)
            implementation(libs.qrgenerator)
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
    namespace = "com.tisto.kmp.helper.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
