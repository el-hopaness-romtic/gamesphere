group = "ru.gamesphere"
version = "1.0-SNAPSHOT"

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("org.projectlombok:lombok:1.18.24")
    compileOnly("com.intellij:annotations:12.0")

    implementation(project(":models"))
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.opencsv:opencsv:5.7.0")
    implementation("org.apache.commons:commons-collections4:4.4")
}
