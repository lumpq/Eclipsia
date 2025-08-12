pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "Eclipsia"

// 자동 include 함수
fun includeIfBuildFile(parentDir: String, prefix: String = "") {
    val baseDir = file(parentDir)
    if (!baseDir.exists()) return

    baseDir.listFiles()
        ?.filter { it.isDirectory }
        ?.forEach { dir ->
            if (File(dir, "build.gradle.kts").exists() || File(dir, "build.gradle").exists()) {
                val path = if (prefix.isEmpty()) ":${dir.name}" else ":$prefix:${dir.name}"
                include(path)
            }
        }
}

// 루트 직속 모듈
includeIfBuildFile(".")

// skills 하위 모듈
includeIfBuildFile("skills", "skills")

// nms 하위 모듈
includeIfBuildFile("nms", "nms")
