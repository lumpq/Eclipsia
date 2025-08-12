plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenCentral()
}

dependencies {

    compileOnly(project(":plugin"))
    // nms:* 모듈 자동 추가
    rootProject.subprojects
        .filter { it.path.startsWith(":nms:") }
        .forEach {
            compileOnly(project(it.path))
        }

    // skills:* 모듈 자동 추가
    rootProject.subprojects
        .filter { it.path.startsWith(":skills:") }
        .forEach {
            compileOnly(project(it.path))
        }

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}