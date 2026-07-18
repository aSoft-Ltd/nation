plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
}

group = "tz.co.asoft"
version = libs.versions.asoft.get()

repositories {
    mavenCentral()
    google()
}

dependencies {
    api(kotlinx.serialization.build.json)
}