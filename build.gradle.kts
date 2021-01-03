plugins {
    kotlin("jvm") version "1.4.21" apply false
    id("org.jetbrains.dokka") version "1.4.10.2" apply false
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("maven")
    id("maven-publish")
    id("java")
}

buildscript {
    configurations.classpath {
        resolutionStrategy {
            activateDependencyLocking()
        }
    }
}

if (File("options.gradle。kts").exists()) {
    apply("options.gradle.kts")
}
group = "me.scoretwo"
version = "2.0-SNAPSHOT"

defaultTasks = mutableListOf("publishToMavenLocal")

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
        maven("https://hub.spigotmc.org/repository/snapshots/")
        maven("https://repo.md-5.net/repository/public/")
    }
}

subprojects {

}