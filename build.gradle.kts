import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.7.20" apply false
}

allprojects {
    repositories {
        mavenCentral()
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