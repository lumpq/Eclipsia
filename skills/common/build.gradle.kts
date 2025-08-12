plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))

    // nms:* 모듈 자동 추가
    rootProject.subprojects
        .filter {
            it.path.startsWith(":skills:") && it.path != ":skills:build" && it.path != ":skills:core"
        }
        .forEach {
            compileOnly(project(it.path))
        }

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
