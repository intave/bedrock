import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0-RC"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "de.jpx3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.opencollab.dev/maven-snapshots")
    maven {
        name = "github"
        url = uri("https://maven.pkg.github.com/intave/access")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly("de.jpx3.intave.access:intave-access:14.3.5")
    compileOnly("org.geysermc.floodgate:api:2.0-SNAPSHOT")
}

bukkit {
    name = "IntaveBedrock"
    main = "de.jpx3.bedrock.BedrockSupportPlugin"
    version = "${project.version}"
    softDepend = listOf("Intave", "IntaveBootstrap", "floodgate")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}