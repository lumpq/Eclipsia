pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "Eclipsia"

// 기본 모듈
include("core", "plugin")

include("nms:v1_20_R1")
include("nms:v1_20_R2")
include("nms:v1_20_R3")
include("nms:v1_20_R4")
include("nms:v1_21_R1")
include("nms:v1_21_R2")
include("nms:v1_21_R3")
include("nms:v1_21_R4")
include("nms:v1_21_R5")


include("skills:core")
include("skills:common")
include("skills:skills")
