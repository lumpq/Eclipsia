plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.github.goooler.shadow") version "8.1.8"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18" apply false
}

group = "io.lumpq126"
version = "1.0.0"

val pluginVersion = version.toString()

// lazy로 선언해서 subprojects가 전부 로드된 뒤 계산되도록 함
val nmsProjects by lazy {
    subprojects.filter {
        it.path.startsWith(":nms:") &&
                it.parent?.name == "nms" &&
                it.name != "build"  // build 모듈 제외
    }
}
val skillsProjects by lazy {
    subprojects.filter {
        it.path.startsWith(":skills:") &&
                it.parent?.name == "skills" &&
                it.name != "build"  // 제외할 모듈 이름
    }
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
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":plugin"))

    // NMS 모듈 → reobf 구성 사용
    nmsProjects.forEach {
        implementation(project(it.path, configuration = "reobf"))
    }

    // Skills 모듈 → shadow 구성 있으면 shadow, 없으면 기본 jar
    skillsProjects.forEach { proj ->
        val cfg = if (proj.configurations.findByName("shadow") != null) "shadow" else "default"
        implementation(project(proj.path, configuration = cfg))
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
        // NMS → reobfJar
        nmsProjects.forEach { dependsOn("${it.path}:reobfJar") }
        // Skills → shadowJar 있으면 shadowJar, 없으면 jar
        skillsProjects.forEach { proj ->
            if (proj.tasks.findByName("shadowJar") != null) {
                dependsOn("${proj.path}:shadowJar")
            } else {
                dependsOn("${proj.path}:jar")
            }
        }

        archiveClassifier.set("")
        archiveFileName.set("Eclipsia-$pluginVersion.jar")

        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")

        relocate("org.bstats", "io.lumpq.shadowed.bstats")
    }

    build {
        dependsOn(shadowJar)
    }

    jar {
        enabled = false
    }

    compileJava.get().dependsOn(clean)
}

val serverPluginsDir = file("C:/Users/user/Desktop/.server/plugins")

tasks.register<Copy>("copyJarToServer") {
    dependsOn(tasks.shadowJar)
    from(tasks.shadowJar.get().archiveFile)
    into(serverPluginsDir)
    rename { "Eclipsia-$pluginVersion.jar" }
}

tasks.build {
    dependsOn("copyJarToServer")
}
