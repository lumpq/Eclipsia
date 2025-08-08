plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenCentral()
}

dependencies {
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}