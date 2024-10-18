plugins {
    java
    `maven-publish`
    signing
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

publishing {
    publications.create<MavenPublication>("maven") {
        groupId = "net.worldseed.multipart"
        artifactId = "WorldSeedEntityEngine"
        version = "11.1.1"

        from(components["java"])
    }

    repositories {
        maven {
            name = "WorldSeed"
            url = uri("https://reposilite.worldseed.online/public")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

dependencies {
    compileOnly(libs.minestom)

    implementation(libs.commons.io)
    implementation(libs.zt.zip)
    implementation(libs.javax.json.api)
    implementation(libs.glassfish.json)
    implementation(libs.mql)

    testImplementation(libs.minestom)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
