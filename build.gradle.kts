import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.process.internal.ExecException
import java.io.ByteArrayOutputStream

plugins {
    id("java")
    id("jacoco")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.yuqingliu.extraenchants"
version = "2.0.2-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper

    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("net.objecthunter:exp4j:0.4.8")

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

tasks.shadowJar {
    with(this) {
        configurations = listOf(project.configurations.shadow.get())
    }

    archiveFileName.set("${project.name}-${versionString()}.jar")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // Generate report after tests have been run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report

    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.2".toBigDecimal() // Code coverage requirement expressed in decimal
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

