plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.johnrengelman.shadow")
    id("maven")
    id("maven-publish")
    id("java")
}

dependencies {
    implementation("org.spongepowered:configurate-xml:4.0.0")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-common"))
        include(dependency("org.spongepowered:configurate-xml:4.0.0"))
    }
    relocate("org.spongepowered.configurate", "me.scoretwo.utils.sponge.configuration")

    classifier = null
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("shadow") {
            shadow.component(this)
        }
    }
}