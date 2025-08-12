plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "io.lumpq126"
version = "1.0.0"
val pluginVersion = project.version.toString()

val skillsProjects = subprojects.filter { it.path.startsWith(":skills") }
val nmsProjects = subprojects.filter { it.path.startsWith(":nms") }

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.citizensnpcs.co/repo")
    }

    dependencies {
        compileOnly("net.citizensnpcs:citizensapi:2.0.37-SNAPSHOT")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// 의존성 설정
dependencies {
    implementation(project(":core"))
    implementation(project(":plugin"))

    skillsProjects.forEach {
        implementation(project(it.path))
    }

    nmsProjects.forEach {
        implementation(project(it.path, configuration = "reobf"))
    }
}

// skills → shadowJar만
skillsProjects.forEach { proj ->
    proj.plugins.apply("io.github.goooler.shadow")
    proj.tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        archiveFileName.set("${proj.name}-$pluginVersion.jar")
    }
}

// nms → shadowJar + reobfJar
nmsProjects.forEach { proj ->
    proj.plugins.apply("io.github.goooler.shadow")
    proj.tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        dependsOn("reobfJar")
        archiveClassifier.set("")
        archiveFileName.set("${proj.name}-$pluginVersion.jar")
    }
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand("version" to pluginVersion)
        }
    }

    shadowJar {
        // nms는 reobf + shadowJar 실행
        nmsProjects.forEach {
            dependsOn("${it.path}:reobfJar")
            dependsOn("${it.path}:shadowJar")
        }
        // skills는 shadowJar만 실행
        skillsProjects.forEach {
            dependsOn("${it.path}:shadowJar")
        }

        archiveClassifier.set("")
        archiveFileName.set("Eclipsia-$pluginVersion.jar")

        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")

        relocate("org.bstats", "io.lumpq.shadowed.bstats")
    }

    jar {
        enabled = false
    }

    compileJava.get().dependsOn(clean)

    val serverPluginsDir = file("C:/Users/user/Desktop/.server/plugins")

    register<Copy>("copyAllJarsToServer") {
        dependsOn(shadowJar)
        from(shadowJar.get().archiveFile)
        from(skillsProjects.map { it.tasks.named("shadowJar") })
        from(nmsProjects.map { it.tasks.named("shadowJar") })
        into(serverPluginsDir)
    }

    build {
        dependsOn("copyAllJarsToServer")
    }
}
