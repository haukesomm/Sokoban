plugins {
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

application {
    mainClass.set("de.haukesomm.sokoban.Sokoban")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Jar> {
    manifest {
        attributes["Manifest-Version"] = "0.1-SNAPSHOT"
        attributes["Main-Class"] = "de.haukesomm.sokoban.Sokoban"
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
