import org.jetbrains.kotlin.config.KotlinCompilerVersion

dependencies {
    implementation("org.spongepowered:configurate-yaml:4.0.0")
}

shadowJar {
    dependencies {
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
        include(dependency("org.spongepowered:configurate-yaml:4.0.0")))
    }
    relocate("org.spongepowered.configurate", "me.scoretwo.utils.sponge.configuration")

    archiveClassifier = null
}
publishing {
    publications {
        shadow(MavenPublication) { publication ->
            project.shadow.component(publication)
        }
    }
}