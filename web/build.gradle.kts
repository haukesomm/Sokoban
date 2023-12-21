import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
}

val fritz2version = rootProject.ext["fritz2version"]

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.fritz2:headless:$fritz2version")
                implementation(project(":core"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("tailwindcss", "3.3.3"))
                implementation(npm("@tailwindcss/forms", "0.5.4"))

                implementation(devNpm("webpack", "5.73.0"))
                implementation(devNpm("socket.io", "4.7.2"))
                implementation(devNpm("postcss", "8.4.31"))
                implementation(devNpm("postcss-loader", "7.3.3"))
                implementation(devNpm("autoprefixer", "10.4.14"))
                implementation(devNpm("css-loader", "6.8.1"))
                implementation(devNpm("style-loader", "3.3.3"))
                implementation(devNpm("cssnano", "6.0.1"))
            }
        }
    }
}

// FIXME: Entfernen, sobald nicht mehr explizit benötigt
tasks.named("jsBrowserDevelopmentRun") {
    dependsOn("jsDevelopmentExecutableCompileSync")
}

tasks.named<ProcessResources>("jsProcessResources") {
    // Always run task:
    outputs.upToDateWhen { false }

    filesMatching("index.html") {
        expand(
            "sokobanVersion" to version,
            "sokobanBuildTime" to ZonedDateTime
                .now(ZoneId.of("Europe/Berlin"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")),
            "fritz2Version" to fritz2version
        )
    }
}

/**
 * KSP support - start
 */
dependencies {
    add("kspCommonMainMetadata", "dev.fritz2:lenses-annotation-processor:$fritz2version")
}
kotlin.sourceSets.commonMain { kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin") }

// Fixes webpack-cli incompatibility by pinning the newest version.
// https://youtrack.jetbrains.com/issue/KTIJ-22030
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") dependsOn("kspCommonMainKotlinMetadata")
}
/**
 * KSP support - end
 */