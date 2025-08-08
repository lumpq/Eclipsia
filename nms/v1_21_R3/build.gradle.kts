plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}