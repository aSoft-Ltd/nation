plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api(kotlinx.serialization.json)
}