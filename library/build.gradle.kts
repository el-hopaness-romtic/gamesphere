group = "ru.gamesphere"
version = "1.0-SNAPSHOT"

plugins {
    java
}

allprojects {
    apply(plugin = "java")

    dependencies {
        implementation("com.intellij:annotations:12.0")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
