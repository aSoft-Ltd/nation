import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask
import nation.flags.GenerateFlagsTask

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("tz.co.asoft.library")
    kotlin("plugin.compose")
    id("flags-generator-compose")
}

description = "All country flags for compose multiplatform"

val generateFlags by tasks.creating(GenerateFlagsTask::class)

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.WASM) wasmJs { library() }
    if (Targeting.OSX) iosTargets() else listOf()

    targets.configureEach {
        compilations.all {
            compileTaskProvider.configure {
                dependsOn(generateFlags)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.nationCountries)
                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)
                api(compose.components.resources)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kommander.core)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose {
    resources {
        packageOfResClass = "nation.generated.resources"
    }
}

rootProject.the<NodeJsRootExtension>().apply {
    version = npm.versions.node.version.get()
    downloadBaseUrl = npm.versions.node.url.get()
}

rootProject.tasks.withType<KotlinNpmInstallTask>().configureEach {
    args.add("--ignore-engines")
}

tasks.named("wasmJsTestTestDevelopmentExecutableCompileSync").configure {
    mustRunAfter(tasks.named("jsBrowserTest"))
}