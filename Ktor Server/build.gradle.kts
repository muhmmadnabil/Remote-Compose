plugins {
    kotlin("jvm") version "2.3.20"
    application
}

repositories {
    google()
    mavenCentral()
}

val ktorVersion = "3.4.0"
val remoteComposeVersion = "1.0.0-alpha08"

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.5.18")

    implementation("androidx.compose.remote:remote-core:$remoteComposeVersion")
    implementation("androidx.compose.remote:remote-creation-core:$remoteComposeVersion")
    implementation("androidx.compose.remote:remote-creation-jvm:$remoteComposeVersion")
}

application {
    mainClass = "com.muhmmad.remotecompose.server.ApplicationKt"
}

kotlin {
    jvmToolchain(17)
}