plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    `maven-publish`
    signing
}

group = "io.lumpq126"
version = "1.0.0"

val pluginVersion = version.toString()

val nmsProjects by lazy {
    subprojects.filter { it.path.startsWith(":nms:") && it.parent?.name == "nms" && it.name != "build" && it.name.isNotEmpty() }
}

val skillsProjects by lazy {
    subprojects.filter { it.path.startsWith(":skills:") && it.parent?.name == "skills" && it.name != "build" && it.name.isNotEmpty() }
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
        maven("https://maven.citizensnpcs.co/repo") { name = "citizens-repo" }
    }

    dependencies {
        compileOnly("net.citizensnpcs:citizensapi:2.0.37-SNAPSHOT")
        compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

// ShadowJar 설정
tasks {
    shadowJar {
        nmsProjects.forEach { dependsOn("${it.path}:reobfJar") }
        skillsProjects.forEach {
            if (it.tasks.findByName("shadowJar") != null) dependsOn("${it.path}:shadowJar")
            else dependsOn("${it.path}:jar")
        }

        archiveClassifier.set("")
        archiveFileName.set("Eclipsia-$pluginVersion.jar")
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")
        relocate("org.bstats", "io.lumpq.shadowed.bstats")
    }

    build { dependsOn(shadowJar) }

    jar { enabled = false }

    compileJava.get().dependsOn(clean)
}

// 서버 테스트용 복사
val serverPluginsDir = file("C:/Users/user/Desktop/.server/plugins")
tasks.register<Copy>("copyJarToServer") {
    dependsOn(tasks.shadowJar)
    from(tasks.shadowJar.get().archiveFile)
    into(serverPluginsDir)
    rename { "Eclipsia-$pluginVersion.jar" }
}
tasks.build { dependsOn("copyJarToServer") }

// Maven 배포 설정
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
}

// Nexus / OSSRH 배포
nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("ossrhUsername") as String?)
            password.set(project.findProperty("ossrhPassword") as String?)
        }
    }
}

// GPG 서명
signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

// Subproject 의존성
dependencies {
    implementation(project(":core"))
    implementation(project(":plugin"))

    nmsProjects.forEach { implementation(project(it.path)) }
    skillsProjects.forEach { proj ->
        val cfg = if (proj.configurations.findByName("shadow") != null) "shadow" else "default"
        implementation(project(proj.path, configuration = cfg))
    }
}
