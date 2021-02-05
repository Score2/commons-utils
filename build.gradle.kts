plugins {
    kotlin("jvm") version "1.4.30"
    id("org.jetbrains.dokka") version "1.4.10.2" apply false
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("maven")
    id("maven-publish")

}

if (File("options.gradle.kts").exists()) {
    apply("options.gradle.kts")
}

defaultTasks = mutableListOf("publishToMavenLocal")

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/sonatype-nexus-snapshots/")
    }
}

subprojects {
    group = "me.scoretwo"
    version = "2.0.7-SNAPSHOT"

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}