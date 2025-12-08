plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("de.haukesomm.sokoban.legacy.SokobanDesktopApp")
}

sourceSets {
    main {
        output.setResourcesDir("build/classes/java/main")
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Manifest-Version"] = project.version
        attributes["Main-Class"] = application.mainClass
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
