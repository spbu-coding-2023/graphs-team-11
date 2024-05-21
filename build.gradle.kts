import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    jacoco
}

group = "org.team11.bdsm.graphs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://raw.github.com/gephi/gephi/mvn-thirdparty-repo/")
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

    implementation("org.gephi", "gephi-toolkit", "0.10.1", classifier = "all")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("skipped", "failed")
        afterSuite(
            // spell
            KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
                // Only execute on the outermost suite
                if (desc.parent == null) {
                    println(" **** Result: ${result.resultType} ****")
                    println("  >    Tests: ${result.testCount}")
                    println("  >   Passed: ${result.successfulTestCount}")
                    println("  >   Failed: ${result.failedTestCount}")
                    println("  >  Skipped: ${result.skippedTestCount}")
                }
            }),
        )
    }

    reports {
        junitXml.required = true
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)
    reports {
        csv.required = true
        xml.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
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
