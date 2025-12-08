import de.haukesomm.sokoban.gradle.npm
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    alias(libs.plugins.google.ksp)
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
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
        jsMain {
            dependencies {
                // tailwind
                implementation(npm(libs.tailwindcss.core))
                implementation(npm(libs.tailwindcss.forms))

                // webpack
                implementation(npm(libs.postcss.core))
                implementation(npm(libs.postcss.loader))
                implementation(npm(libs.autoprefixer))
                implementation(npm(libs.css.loader))
                implementation(npm(libs.style.loader))
                implementation(npm(libs.cssnano))
                
                implementation(npm("mini-css-extract-plugin", libs.versions.minicssextractplugin.get()))
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

tasks.named<Sync>("jsBrowserDistribution") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

// KSP support for Lens generation
dependencies {
    kspCommonMainMetadata(libs.fritz2.lensesprocessor)
}

project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}