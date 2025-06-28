
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Pkg
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.UUID

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.dokka)
    kotlin("plugin.serialization") version "2.0.20"
    id("com.github.gmazzo.buildconfig") version "5.5.1"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Neutron"
            isStatic = true
        }
    }
    
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "Neutron.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.app.update)
            implementation(libs.app.update.ktx)
            implementation(libs.review)
            implementation(libs.review.ktx)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.biometric)
            implementation(libs.androidx.appcompat)
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.equinox.compose)
                implementation(libs.equinox.core)
                implementation(libs.precompose)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.lazy.pagination.compose)
                implementation(libs.material3.window.size)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor3)
                implementation(libs.filekit.core)
                implementation(libs.filekit.compose)
                implementation(libs.datetime.wheel.picker)
                implementation(libs.kotlinx.datetime)
                implementation(libs.colorpicker.compose)
                //implementation(libs.ametista.engine)
                implementation(libs.neutroncore)
            }
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.octocatkdu)
            implementation(libs.ktor.client.okhttp)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

    }
}

android {
    namespace = "com.tecknobit.neutron"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.tecknobit.neutron"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 6
        versionName = "1.0.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.tecknobit.neutron.MainKt"
        nativeDistributions {
            targetFormats(Deb, Pkg, Exe)
            modules(
                "java.compiler", "java.instrument", "java.management", "java.naming", "java.net.http", "java.prefs",
                "java.rmi", "java.scripting", "java.security.jgss", "java.sql", "jdk.jfr", "jdk.unsupported",
                "jdk.security.auth"
            )
            packageName = "Neutron"
            packageVersion = "1.0.3"
            packageName = "Neutron"
            packageVersion = "1.0.3"
            version = "1.0.3"
            description = "Order and ticket revenue manager for the projects you are developing"
            copyright = "© 2025 Tecknobit"
            vendor = "Tecknobit"
            licenseFile.set(project.file("src/desktopMain/resources/LICENSE"))
            macOS {
                bundleID = "com.tecknobit.neutron"
                iconFile.set(project.file("src/desktopMain/resources/logo.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/logo.ico"))
                upgradeUuid = UUID.randomUUID().toString()
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/logo.png"))
                packageName = "com-tecknobit-neutron"
                debMaintainer = "infotecknobitcompany@gmail.com"
                appRelease = "1.0.3"
                appCategory = "PERSONALIZATION"
                rpmLicenseType = "APACHE2"
            }
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("src/desktopMain/resources/compose-desktop.pro"))
            version.set("7.5.0")
            obfuscate.set(true)
        }
    }
}

configurations.all {
    exclude("io.github.n7ghtm4r3", "Equinox-Compose-android")
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        moduleName.set("Neutron")
        outputDirectory.set(layout.projectDirectory.dir("../docs"))
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customAssets = listOf(file("../docs/logo-icon.svg"))
        footerMessage = "(c) 2025 Tecknobit"
    }
}

buildConfig {
    className("AmetistaConfig")
    packageName("com.tecknobit.ametista")
    buildConfigField<String>(
        name = "HOST",
        value = project.findProperty("host").toString()
    )
    buildConfigField<String?>(
        name = "SERVER_SECRET",
        value = project.findProperty("server_secret").toString()
    )
    buildConfigField<String?>(
        name = "APPLICATION_IDENTIFIER",
        value = project.findProperty("application_id").toString()
    )
    buildConfigField<Boolean>(
        name = "BYPASS_SSL_VALIDATION",
        value = project.findProperty("bypass_ssl_validation").toString().toBoolean()
    )
}