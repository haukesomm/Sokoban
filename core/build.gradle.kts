plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js(BOTH)

    sourceSets {
        val commonMain by getting
    }
}