plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        withJava()
    }
    js(BOTH)

    sourceSets {
        val jvmMain by getting
    }
}