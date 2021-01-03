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
    testImplementation("com.andreapivetta.kolor:kolor:1.0.0")
    testImplementation("junit:junit:4.13.1")
    implementation("net.md-5:bungeecord-chat:1.16-R0.3")
}

shadowJar {
    dependencies {
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
        include(dependency("net.md-5:bungeecord-chat:1.16-R0.3"))
    }
    archiveClassifier = null
}

publishing {
    publications {
        shadow(MavenPublication) { publication ->
            project.shadow.component(publication)
        }
    }
}