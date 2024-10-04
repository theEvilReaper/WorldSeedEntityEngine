rootProject.name = "WorldSeedEntityEngine"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("junit", "5.11.2")
            version("minestom", "2f5bb97908")
            version("commons-io", "2.11.0")
            version("zt-zip", "1.8")
            version("javax.json", "1.1.4")
            version("mql", "1.0.1")
            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")

            library("minestom", "net.minestom", "minestom-snapshots").versionRef("minestom")
            library("commons.io", "commons-io", "commons-io").versionRef("commons-io")
            library("zt.zip", "org.zeroturnaround", "zt-zip").versionRef("zt-zip")
            library("javax.json.api", "javax.json", "javax.json-api").versionRef("javax.json")
            library("glassfish.json", "org.glassfish", "javax.json").versionRef("javax.json")
            library("mql", "dev.hollowcube", "mql").versionRef("mql")
        }
    }
}
