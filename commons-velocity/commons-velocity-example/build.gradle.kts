import org.jetbrains.kotlin.config.KotlinCompilerVersion
plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.johnrengelman.shadow")
    id("maven")
    id("maven-publish")

}

repositories {
    maven("https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    compileOnly("org.ow2.asm:asm-all:5.2")
    compile("com.velocitypowered:velocity-api:1.1.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:1.1.0-SNAPSHOT")//

    implementation(project(":commons-server"))

    implementation(project(":commons-syntaxes"))
    implementation(project(":commons-velocity:commons-velocity-plugin"))
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    dependencies {
        include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        include(dependency("org.jetbrains.kotlin:kotlin-stdlib-common"))

        include(dependency(":commons-server"))

        include(dependency(":commons-syntaxes"))
        include(dependency(":commons-velocity-plugin"))
    }
    classifier = null
}

/*
publishing {
    publications {
        create<MavenPublication>(project.name) {
            project.shadow.component(this)
        }
    }

    publications.withType<MavenPublication> {
        project.shadow.component(this)
    }
}*/
