plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    `java-library`
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("Eclipsia Core API")
                description.set("API module for Eclipsia Minecraft plugin")
                url.set("https://github.com/lumpq/Eclipsia")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("lumpq")
                        name.set("lumpq")
                        email.set("yeonggyu915@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/lumpq/Eclipsia.git")
                    developerConnection.set("scm:git:ssh://github.com/lumpq/Eclipsia.git")
                    url.set("https://github.com/lumpq/Eclipsia")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("ossrhUsername") as String? ?: ""
                password = project.findProperty("ossrhPassword") as String? ?: ""
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}