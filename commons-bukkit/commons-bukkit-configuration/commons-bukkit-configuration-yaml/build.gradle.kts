import org.jetbrains.kotlin.config.KotlinCompilerVersion

dependencies {
    compileOnly("commons-lang:commons-lang:2.6")
    compileOnly("com.google.guava:guava:22.0")
    implementation("org.yaml:snakeyaml:1.27")
}

shadowJar {
    dependencies {
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
        include(dependency("org.yaml:snakeyaml:1.27")))
    }
    relocate("org.yaml.snakeyaml", "me.scoretwo.utils.libs.snakeyaml")

    archiveClassifier = null
}
publishing {
    publications {
        shadow(MavenPublication) { publication ->
            project.shadow.component(publication)
        }
    }
}