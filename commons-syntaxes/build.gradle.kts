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