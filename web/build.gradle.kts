import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm() // needed for kspCommonMainMetadata
    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        commonMain {
            dependencies {
                implementation(libs.fritz2.headless)
                implementation(project(":core"))
            }
        }
        jsMain {
            dependencies {
                implementation(npm("tailwindcss", libs.versions.npm.tailwindcss.asProvider().get()))
                implementation(npm("@tailwindcss/forms", libs.versions.npm.tailwindcss.forms.get()))

                implementation(devNpm("webpack", libs.versions.npm.webpack.get()))
                implementation(devNpm("socket.io", libs.versions.npm.socketio.get()))
                implementation(devNpm("postcss", libs.versions.npm.postcss.asProvider().get()))
                implementation(devNpm("postcss-loader", libs.versions.npm.postcss.loader.get()))
                implementation(devNpm("autoprefixer", libs.versions.npm.autoprefixer.get()))
                implementation(devNpm("css-loader", libs.versions.npm.cssloader.get()))
                implementation(devNpm("style-loader", libs.versions.npm.styleloader.get()))
                implementation(devNpm("cssnano", libs.versions.npm.cssnano.get()))
            }
        }
    }
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
            "fritz2Version" to libs.versions.fritz2.get()
        )
    }
}

/**
 * KSP support - start
 */
dependencies {
    add("kspCommonMainMetadata", libs.fritz2.lensesprocessor)
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