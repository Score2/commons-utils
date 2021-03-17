import org.jetbrains.kotlin.config.KotlinCompilerVersion
plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.johnrengelman.shadow")
    id("maven")
    id("maven-publish")

}
dependencies {
    compileOnly("cn.nukkit:nukkit:1.0-SNAPSHOT")
    compileOnly("org.ow2.asm:asm-all:5.2")
    implementation(project(":commons-server"))

    implementation(project(":commons-syntaxes"))
    implementation("net.md-5:bungeecord-chat:1.16-R0.3")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-common"))

        include(dependency(":commons-server"))

        include(dependency(":commons-syntaxes"))
    }
    classifier = null
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("shadow") {
            shadow.component(this)
        }
    }
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
