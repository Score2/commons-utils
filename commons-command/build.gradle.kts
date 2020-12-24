import org.jetbrains.kotlin.config.KotlinCompilerVersion

dependencies {
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