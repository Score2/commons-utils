import org.jetbrains.kotlin.config.KotlinCompilerVersion

dependencies {
    for (p in parent.getAllprojects()) {
        if (p.name == "commons-utils" || p.name == "commons-integration") continue

        if (!File(p.projectDir, "build.gradle.kts").exists()) continue

        implementation(p)
    }
}

shadowJar {
    dependencies {
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))
        exclude(dependency(kotlin("stdlib", KotlinCompilerVersion.VERSION)))

        for (p in parent.getAllprojects()) {
            if (p.name == "commons-utils" || p.name == "commons-integration") continue

        /*for (DependencyResult dependencyResult : p.getConfigurations().getByName("compileClasspath").getIncoming().getResolutionResult().getAllDependencies()) {
            val from: ResolvedComponentResult = dependencyResult.getFrom()
            println(from.toString())
        }*/


            include(dependency(p))
//            include("${group.toString()}:${p.name}:${archiveVersion.toString()}")
        }
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