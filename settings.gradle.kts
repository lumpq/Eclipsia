pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "Eclipsia"

include("api")
include("core")
include("plugin")
include("nms:v1_19_R3")
include("nms:v1_20_R1")
include("nms:v1_20_R2")
include("nms:v1_20_R3")
include("nms:v1_20_R4")
include("nms:v1_21_R1")
include("nms:v1_21_R2")
include("nms:v1_21_R3")
include("nms:v1_21_R4")
include("nms:v1_21_R5")
