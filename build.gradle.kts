plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "io.github.snowyblossom126"
version = "1.0.0"

val pluginVersion = version.toString()

val nmsProjects by lazy {
    subprojects.filter { it.path.startsWith(":nms:") && it.parent?.name == "nms" && it.name != "build" && it.name.isNotEmpty() }
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
        maven("https://repo.dmulloy2.net/repository/public/") { name = "dmulloy2-repo" }
        maven("https://maven.citizensnpcs.co/repo") { name = "citizens-repo" }
    }

    dependencies {
        compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
        compileOnly("net.citizensnpcs:citizensapi:2.0.37-SNAPSHOT")
        implementation("io.github.snowyblossom126:element-api:1.0.2")
        implementation("io.github.snowyblossom126:enchant-api:1.0.0")
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

tasks {
    shadowJar {
        nmsProjects.forEach { dependsOn("${it.path}:reobfJar") }

        archiveClassifier.set("")
        archiveFileName.set("Eclipsia-$pluginVersion.jar")
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")
        relocate("org.bstats", "io.lumpq.shadowed.bstats")
    }

    build {
        dependsOn(shadowJar)
        finalizedBy("copyJarToServer")
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

dependencies {
    implementation(project(":eclipsia-core"))

    nmsProjects.forEach { implementation(project(it.path)) }
}

nmsProjects.forEach { nmsProject ->
    project(nmsProject.path) {
        dependencies {
            implementation(project(":eclipsia-core"))
        }
    }
}
