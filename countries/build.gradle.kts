import nation.countries.GenerateCountriesTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("countries-generator")
}

description = "A kotlin multiplatform library for offline country support"

val generateCountries by tasks.registering(GenerateCountriesTask::class)

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.WASM) wasmJs { library() }
    if (Targeting.WASM) wasmWasi { library() }
    if (Targeting.OSX) osxTargets() else listOf()
    if (Targeting.NDK) ndkTargets() else listOf()
    if (Targeting.LINUX) linuxTargets() else listOf()
    if (Targeting.MINGW) mingwTargets() else listOf()

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(generateCountries.map { it.outputDir})
            dependencies {
                api(kotlinx.serialization.core)
            }
        }
    }
}

tasks.configureEach {
    if(name!=::generateCountries.name) dependsOn(generateCountries)
}