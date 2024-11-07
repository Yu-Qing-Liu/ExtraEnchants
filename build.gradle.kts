import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.process.internal.ExecException
import java.io.ByteArrayOutputStream

plugins {
    id("java")
}

group = "com.github.yuqingliu.extraenchants"
version = "2.0.2-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper

    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${project.property("paper_version")}")
    compileOnly("org.projectlombok:lombok:${project.property("lombok_version")}")

    annotationProcessor("org.projectlombok:lombok:${project.property("lombok_version")}")
    testCompileOnly("org.projectlombok:lombok:${project.property("lombok_version")}")
    testAnnotationProcessor("org.projectlombok:lombok:${project.property("lombok_version")}")

    implementation(project(":api"))
    implementation("com.google.inject:guice:7.0.0")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.3.1")
}

fun currentBranch(): String {
    return try {
        val stdout = ByteArrayOutputStream()
        project.exec {
            commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
            standardOutput = stdout
        }

        val branchName = stdout.toString()
        branchName.trim().replace("/", "+")
    } catch (e: ExecException) {
        println("Error executing Git command: ${e.message}")
        "undefined"
    }
}

fun versionString(): String {
    val branchName: String = currentBranch()
    val releaseType: String = if (branchName == "master") "release" else "dev+${branchName}"
    return "$version".replace("-SNAPSHOT", "-$releaseType")
}

fun archiveName(): String {
    return "${project.name}-${versionString()}.jar"
}

tasks.register("printArchiveFileName") {
    doLast {
        println(archiveName())
    }
}

// Version Injection
tasks.processResources {
    val fullVersion: String = versionString()

    inputs.property("fullVersion", fullVersion)
    filesMatching("**/plugin.yml") {
        filter<ReplaceTokens>(
                "beginToken" to "\${",
                "endToken" to "}",
                "tokens" to mapOf(
                        "full.version" to fullVersion
                )
        )
    }

    println("Finished injecting version: $fullVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Jar>("uberJar") {
    archiveClassifier = "uber"

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.jar {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.named("uberJar"))
}

tasks.build {
    dependsOn(tasks.assemble)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
