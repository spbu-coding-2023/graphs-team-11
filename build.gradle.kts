import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "org.team11.bdsm.graphs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

val exposedVersion = "0.50.1"
val sqliteJdbcVersion = "3.41.2.2"
dependencies {
    implementation(compose.desktop.currentOs)
    implementation(kotlin("reflect"))

    // JetBrains Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    // SQLite JDBC
    implementation("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")

    // Logging
    implementation("org.slf4j", "slf4j-simple", "2.0.13")

    // File Picker
    implementation("com.darkrockstudios:mpfilepicker:3.1.0")

    // XML Serialization
    implementation("io.github.pdvrieze.xmlutil:serialization:0.86.3")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Graphs Team 11"
            packageVersion = "1.0.0"
        }
    }
}
