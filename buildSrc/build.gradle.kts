plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.7.10"
}

repositories {
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.aliyun.com/repository/jcenter")
    google()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:7.2.0")
    implementation("commons-io:commons-io:2.6")
    implementation("commons-codec:commons-codec:1.15")
    implementation("org.ow2.asm:asm-commons:9.3")
    implementation("org.ow2.asm:asm-tree:9.3")
}