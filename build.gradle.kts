import java.text.SimpleDateFormat

plugins {
    id("java-library")
    id("maven-publish")
    id("com.github.johnrengelman.shadow").version("7.1.2")
}

repositories {
    mavenLocal()
    //CrypticLib
    maven("http://110.42.10.241:8082/repository/maven-public/") {
        isAllowInsecureProtocol = true
    }
    //PlaceholderAPI
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
//    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("com.crypticlib:common:0.17.7")
    implementation("com.crypticlib:action:0.17.7")
}

group = "pers.yufiria"
version = "1.0.3"
var mainClass = "pers.yufiria.playerInvMenu.PlayerInvMenu"
var pluginVersion: String = version.toString() + "-" + SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis())
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    val props = HashMap<String, String>()
    props["version"] = pluginVersion
    props["main"] = mainClass
    props["name"] = rootProject.name
    processResources {
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    compileJava {
        dependsOn(clean)
        options.encoding = "UTF-8"
    }
    shadowJar {
        relocate("crypticlib", "pers.yufiria.playerInvMenu.crypticlib")
        archiveFileName.set("${rootProject.name}-${version}.jar")
    }
    assemble {
        dependsOn(shadowJar)
    }
}
