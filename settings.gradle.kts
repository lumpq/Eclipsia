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
include("v1_17_R1")
include("v1_18_R1")
include("v1_18_R2")
include("v1_19_R1")
include("v1_19_R2")
include("v1_19_R3")
include("v1_20_R1")
include("v1_20_R2")
include("v1_20_R3")
include("v1_20_R4")
include("v1_21_R1")
include("v1_21_R2")
include("v1_21_R3")
include("v1_21_R4")
include("v1_21_R5")
