plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.johnrengelman.shadow")
    id("maven")
    id("maven-publish")

}

dependencies {
    implementation(project(":commons-server"))
    implementation(project(":commons-language"))
    implementation("commons-lang:commons-lang:2.6")

    testImplementation("com.andreapivetta.kolor:kolor:1.0.0")
    testImplementation("junit:junit:4.13.1")
    implementation("net.md-5:bungeecord-chat:1.16-R0.5-SNAPSHOT")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-common"))
        include(dependency("net.md-5:bungeecord-chat:1.16-R0.3"))
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