import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.7.20" apply false
    id("com.google.devtools.ksp") version "1.7.21-1.0.8" apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
    group = "de.haukesomm.sokoban"
    version = "0.1-SNAPSHOT"

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

ext {
    set("fritz2version", "1.0-RC11")
    set("coroutinesVersion", "1.6.4")
    set("serializationVersion", "1.3.0")
}