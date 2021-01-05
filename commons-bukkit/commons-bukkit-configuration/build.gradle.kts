import org.jetbrains.kotlin.config.KotlinCompilerVersion
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
    compileOnly("commons-lang:commons-lang:2.6")
    compileOnly("com.google.guava:guava:22.0")
    implementation("org.yaml:snakeyaml:1.27")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-common"))
        include(dependency("org.yaml:snakeyaml:1.27"))
    }
    relocate("org.yaml.snakeyaml", "me.scoretwo.utils.libs.snakeyaml")

    classifier = null
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("shadow") {
            shadow.component(this)
        }
    }
}