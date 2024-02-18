import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.9.22" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.16" apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
    group = "de.haukesomm.sokoban"
    version = "0.2-SNAPSHOT"

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

ext {
    set("fritz2version", "1.0-RC16")
    set("coroutinesVersion", "1.7.3")
    set("serializationVersion", "1.6.2")
}