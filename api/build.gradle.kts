plugins {
    id("java")
}

group = "com.github.yuqingliu.extraenchants.api"
version = parent!!.version

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper

    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${project.parent?.property("paper_version")}")

    compileOnly("org.projectlombok:lombok:${project.property("lombok_version")}")
    annotationProcessor("org.projectlombok:lombok:${project.property("lombok_version")}")
}

tasks.test {
    useJUnitPlatform()
}
