plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "io.lumpq126"
version = "1.0.0"

val pluginVersion = version.toString()

val nmsProjects by lazy {
    subprojects.filter { it.path.startsWith(":nms:") && it.parent?.name == "nms" && it.name != "build" && it.name.isNotEmpty() }
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
        maven("https://maven.citizensnpcs.co/repo") { name = "citizens-repo" }
        maven("https://repo.nightexpressdev.com/releases") { name = "nightexpress-releases" }
    }

    dependencies {
        compileOnly("net.citizensnpcs:citizensapi:2.0.37-SNAPSHOT")
        compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
        compileOnly("io.lumpq126:enchantapi:1.0.0")
        compileOnly("io.lumpq126:elementapi:1.0.0")
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

    tasks.withType<Javadoc>().configureEach {
        enabled = false
    }
}

// ShadowJar 설정
tasks {
    shadowJar {
        nmsProjects.forEach { dependsOn("${it.path}:reobfJar") }



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

// Subproject 의존성
dependencies {
    implementation(project(":api"))
    implementation(project(":core"))

    nmsProjects.forEach { implementation(project(it.path)) }
}
