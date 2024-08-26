rootProject.name = "WorldSeedEntityEngine"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("junit", "5.11.0")
            version("minestom", "2f5bb97908")
            version("commons-io", "2.11.0")
            version("zt-zip", "1.8")
            version("javax.json", "1.1.4")
            version("mql", "1.0.1")

            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").version("junit")
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").version("junit")

            library("minestom", "net.minestom", "minestom-snapshots").version("minestom")
            library("commons.io", "commons-io", "commons-io").version("commons-io")
            library("zt.zip", "org.zeroturnaround", "zt-zip").version("zt-zip")
            library("javax.json.api", "javax.json", "javax.json-api").version("javax.json")
            library("javax.json", "org.glassfish", "javax.json").version("javax.json")
            library("mql", "dev.hollowcube", "mql").version("mql")
        }
    }
}
