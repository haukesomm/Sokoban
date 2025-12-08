import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId) apply false
    id(libs.plugins.kotlin.jvm.get().pluginId) apply false
    alias(libs.plugins.google.ksp) apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

subprojects {
    group = "de.haukesomm.sokoban"
    version = "0.3.0-SNAPSHOT"

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
}