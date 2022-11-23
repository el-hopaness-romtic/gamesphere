import org.jooq.meta.jaxb.Logging

plugins {
    id("java")
    id("nu.studer.jooq") version "8.0"
}

group = "ru.gamesphere"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("org.projectlombok:lombok:1.18.24")

    implementation("org.postgresql:postgresql:42.5.0")
    jooqGenerator("org.postgresql:postgresql:42.5.0")

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.jooq:jooq:3.17.5")
    implementation("org.jooq:jooq-meta:3.17.5")
    implementation("org.jooq:jooq-codegen:3.17.5")
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/postgres"
                    user = "postgres"
                    password = "12345678"
                    // properties.add(Property().apply {
                    //     key = "ssl"
                    //     value = "true"
                    // })
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "flyway_schema_history"
                        // forcedTypes.addAll(listOf(
                        //         ForcedType().apply {
                        //             name = "varchar"
                        //             includeExpression = ".*"
                        //             includeTypes = "JSONB?"
                        //         },
                        //         ForcedType().apply {
                        //             name = "varchar"
                        //             includeExpression = ".*"
                        //             includeTypes = "INET"
                        //         }
                        // ))
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "ru.gamesphere.domain"
                        directory = "build/generated-sources/jooq"  // default (can be omitted)
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
