pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "Eclipsia"

// 기본 모듈
include("core", "plugin")

// nms 디렉토리 자동 포함
file("nms").listFiles()
    ?.filter { it.isDirectory }
    ?.forEach { dir ->
        include("nms:${dir.name}")
    }

// skills 디렉토리 자동 포함 (있다면)
file("skills").listFiles()
    ?.filter { it.isDirectory }
    ?.forEach { dir ->
        include("skills:${dir.name}")
    }
