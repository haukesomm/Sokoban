plugins {
    alias(libs.plugins.kotlin.jvm)
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
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
