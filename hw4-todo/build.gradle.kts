plugins {
    kotlin("jvm") version "1.7.10"
    id("net.serenity-bdd.serenity-gradle-plugin") version "3.5.0"
}

group = "ru.geracimov.otus.kotlinqa.hw"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.22")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("io.mockk:mockk:1.13.3")

    testImplementation("net.serenity-bdd:serenity-core:3.5.1")
    testImplementation("net.serenity-bdd:serenity-junit5:3.5.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.junit.platform:junit-platform-suite:1.9.0")
    testImplementation("io.cucumber:cucumber-jvm:7.10.1")
    testImplementation("io.cucumber:cucumber-java8:7.10.1")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.10.1")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}