plugins {
    kotlin("jvm") version "1.7.10"
}

group = "ru.geracimov.otus.kotlinqa.hw"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("io.mockk:mockk:1.13.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}