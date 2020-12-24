buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:5.2.0")
        classpath(kotlin("gradle-plugin", version = "1.4.21"))
    }
}
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("java")
}
if (File("options.gradle").exists()) {
    apply("options.gradle")
}

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
    apply("java")
    apply("kotlin")
    apply("maven")
    apply("maven-publish")
    apply("com.github.johnrengelman.shadow")

    group = "me.scoretwo"
    version = "2.0-SNAPSHOT"

    dependencies {
        testImplementation("com.andreapivetta.kolor:kolor:1.0.0")
        testImplementation("junit:junit:4.13.1")
    }

    tasks {
        val sourceJar by registering(Jar::class) {
            from(sourceSets.main)
        }
    }

}