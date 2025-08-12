import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "io.lumpq126"
version = "1.0.0"
val pluginVersion = project.version.toString()

// 하위 모듈 분류
val skillsProjects = subprojects.filter { it.path.startsWith(":skills") }
val nmsProjects = subprojects.filter { it.path.startsWith(":nms") }

// NMS 디렉토리명 → 가능한 Paper 버전 목록
val nmsVersionMap = mapOf(
    "v1_21_R5" to listOf("1.21.8", "1.21.7", "1.21.6"),
    "v1_21_R4" to listOf("1.21.5"),
    "v1_21_R3" to listOf("1.21.4"),
    "v1_21_R2" to listOf("1.21.3"),
    "v1_21_R1" to listOf("1.21.1"),
    "v1_20_R4" to listOf("1.20.6", "1.20.5"),
    "v1_20_R3" to listOf("1.20.4", "1.20.3"),
    "v1_20_R2" to listOf("1.20.2"),
    "v1_20_R1" to listOf("1.20.1", "1.20")
)

// CLI에서 -PnmsTarget=1.21.7 지정 가능
val nmsTargetVersion: String? = project.findProperty("nmsTarget") as String?

// NMS 버전 매핑 (디렉토리명 → 선택된 Paper 버전 SNAPSHOT)
val nmsVersions: Map<String, String> = nmsProjects.associate { proj ->
    val dirName = proj.name
    val target = nmsTargetVersion?.takeIf { it in (nmsVersionMap[dirName] ?: emptyList()) }
        ?: nmsVersionMap[dirName]?.firstOrNull()
        ?: error("No Paper version mapping found for $dirName")
    "nms:${dirName}" to "$target-R0.1-SNAPSHOT"
}

println("Using NMS versions: $nmsVersions")

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

// 메인 프로젝트 의존성
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
    proj.tasks.named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        archiveFileName.set("${proj.name}-$pluginVersion.jar")
    }
}

// nms → shadowJar + reobfJar
nmsProjects.forEach { proj ->
    proj.plugins.apply("io.github.goooler.shadow")
    proj.tasks.named<ShadowJar>("shadowJar") {
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
        // nms: reobfJar + shadowJar
        nmsProjects.forEach {
            dependsOn("${it.path}:reobfJar")
            dependsOn("${it.path}:shadowJar")
        }
        // skills: shadowJar
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
