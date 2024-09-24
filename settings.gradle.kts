pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("multimodule")
}

fun includeSubs(base: String, path: String = base, vararg subs: String) {
    subs.forEach {
        include(":$base-$it")
        project(":$base-$it").projectDir = File("$path/$it")
    }
}

listOf("kommander").forEach {
    includeBuild("../$it")
}

rootProject.name = "nation"

includeBuild("build-logic-nation")

includeSubs("nation", ".", "countries", "currencies")
includeSubs("nation-flags", "flags", "compose")