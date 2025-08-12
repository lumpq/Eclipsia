plugins {
    id ("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.github.goooler.shadow") version "8.1.8"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18" apply false
}

group = "io.lumpq126"
version = "1.0.0"

val pluginVersion = project.version.toString()

val nmsVersions = mapOf(
    "v1_17_R1" to "1.17.1-R0.1-SNAPSHOT",
    "v1_18_R1" to "1.18.1-R0.1-SNAPSHOT",
    "v1_18_R2" to "1.18.2-R0.1-SNAPSHOT",
    "v1_19_R1" to "1.19.2-R0.1-SNAPSHOT",
    "v1_19_R2" to "1.19.3-R0.1-SNAPSHOT",
    "v1_19_R3" to "1.19.4-R0.1-SNAPSHOT",
    "v1_20_R1" to "1.20.1-R0.1-SNAPSHOT",
    "v1_20_R2" to "1.20.2-R0.1-SNAPSHOT",
    "v1_20_R3" to "1.20.4-R0.1-SNAPSHOT",
    "v1_20_R4" to "1.20.6-R0.1-SNAPSHOT",
    "v1_21_R1" to "1.21.1-R0.1-SNAPSHOT",
    "v1_21_R2" to "1.21.2-R0.1-SNAPSHOT",
    "v1_21_R3" to "1.21.4-R0.1-SNAPSHOT",
    "v1_21_R4" to "1.21.5-R0.1-SNAPSHOT",
    "v1_21_R5" to "1.21.8-R0.1-SNAPSHOT"
)

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
        maven("https://maven.citizensnpcs.co/repo") { name = "citizens-repo" }
    }

    dependencies {
        //implementation("net.dv8tion:JDA:5.0.0")
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
    nmsVersions.keys.forEach { implementation(project(":$it", configuration = "reobf")) }
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand("version" to pluginVersion)
        }
    }

    shadowJar {
        nmsVersions.keys.forEach { dependsOn(":$it:reobfJar") }

        archiveClassifier.set("")
        archiveFileName.set("Eclipsia-$pluginVersion.jar")

        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
        exclude("module-info.class")

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
    rename { "Eclipsia-${pluginVersion}.jar" }
}

tasks.build {
    dependsOn("copyJarToServer")
}

bukkit {
    name = "Eclipsia"
    main = "io.lumpq126.eclipsia.EclipsiaPlugin"
    version = pluginVersion
    apiVersion = "1.19"
    authors = listOf("_LumPq_")
    commands {
        register("fish") {
            permission = "op"
            aliases = listOf("f")
        }
        register("month") {
            permission = "op"
        }
        register("eclipsia") {
            permission = "op"
            aliases = listOf("e", "ec", "ecc", "ep")
        }
    }
}
