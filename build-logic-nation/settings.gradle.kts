pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencyResolutionManagement {
        versionCatalogs {
            file("../../versions/gradle/versions").listFiles().map {
                it.nameWithoutExtension to it.absolutePath
            }.forEach { (name, path) ->
                create(name) { from(files(path)) }
            }
        }
    }
}

rootProject.name = "build-logic-nation"