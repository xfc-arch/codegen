plugins {
    kotlin("jvm") version "2.2.20"
}

group = "com.xfc-arch"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.xfc-arch:codegen-core:$version")
    implementation("com.xfc-arch:spring-agents:$version")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
