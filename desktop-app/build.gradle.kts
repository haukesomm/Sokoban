plugins {
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

application {
    mainClass.set("de.haukesomm.sokoban.app.desktop.SokobanDesktopApp")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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
