import nation.currencies.CurrencyGenerator
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("currency-generator")
}

description = "A kotlin multiplatform library to deal with currencies"

val dir = layout.buildDirectory.dir("generated/currencies/kotlin")
val generateCurrencies by tasks.registering(CurrencyGenerator::class) {
    outputDir.set(dir)
}

kotlin {
    jvm { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.WASM) wasmJs { library() }
    if (Targeting.WASM) wasmWasi { library() }
    if (Targeting.OSX) osxTargets() else listOf()
    if (Targeting.NDK) ndkTargets() else listOf()
    if (Targeting.LINUX) linuxTargets() else listOf()
    if (Targeting.MINGW) mingwTargets() else listOf()

//    targets.configureEach {
//        compilations.all {
//            compileTaskProvider.configure {
//                dependsOn(generateCurrencies)
//            }
//        }
//    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(dir)
            dependencies {
                api(kotlinx.serialization.core)
                api(projects.nationCountries)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kommander.core)
                implementation(kotlinx.serialization.json)
            }
        }
    }
}

tasks.configureEach {
    if(name!=::generateCurrencies.name) dependsOn(generateCurrencies)
}

tasks.named("androidNativeArm32SourcesJar").configure {
    println("====".repeat(4))
    println(this::class.qualifiedName)
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
    mustRunAfter(tasks.named("jsNodeTest"))
}