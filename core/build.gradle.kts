plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js(BOTH)

    sourceSets {
        val jvmMain by getting
    }
}