plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.johnrengelman.shadow")
    id("maven")
    id("maven-publish")

}

dependencies {
    testImplementation("com.andreapivetta.kolor:kolor:1.0.0")
    testImplementation("junit:junit:4.13.1")
    compileOnly("org.slf4j:slf4j-log4j12:1.7.30")
    compileOnly("org.ow2.asm:asm-all:5.2")
    compileOnly("net.md-5:bungeecord-chat:1.16-R0.5-SNAPSHOT")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-common"))
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