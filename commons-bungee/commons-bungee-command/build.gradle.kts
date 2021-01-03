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
    compileOnly("net.md-5:bungeecord-api:1.16-R0.3")
}

shadowJar {
    dependencies {
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
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