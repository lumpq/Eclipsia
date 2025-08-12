plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))

    rootProject.subprojects
        .filter { it.path.startsWith(":nms:") }
        .forEach {
            compileOnly(project(it.path))
        }

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}