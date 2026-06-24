plugins {
    id("net.neoforged.moddev")
    id ("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')
version = "${property("mod.version")}+${property("deps.minecraft")}"

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = "$version"
        this["minecraft"] = prop("mod.mc_dep_forgelike")
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
        expand(props)
    }
}

version = "$version-neoforge"
base.archivesName = property("mod.id") as String

jsonlang {
    languageDirectories = listOf("assets/${property("mod.id")}/lang")
    prettyPrint = true
}

repositories {
    maven {
        name = "Parchment Mappings"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroupAndSubgroups("maven.modrinth")
        }
    }
    maven {
        name = "Terraformers (Mod Menu)"
        url = uri("https://maven.terraformersmc.com/releases/")
        content {
            includeGroupAndSubgroups("com.terraformersmc")
            includeGroupAndSubgroups("dev.emi")
        }
    }
    maven {
        name = "Architectury"
        url = uri("https://maven.architectury.dev/")
        content {
            includeGroupAndSubgroups("dev.architectury")
            includeGroupAndSubgroups("me.shedaniel")
        }
    }
    maven {
        name = "CC: Tweaked"
        url = uri("https://maven.squiddev.cc")
        content {
            includeGroupAndSubgroups("cc.tweaked")
        }
    }
    maven {
        name = "Create, Ponder, Flywheel"
        url = uri("https://maven.createmod.net")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("com.simibubi")
        }
    }
    maven {
        name = "Registrate"
        url = uri("https://maven.ithundxr.dev/snapshots")
        content {
            includeGroupAndSubgroups("com.tterrag")
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "Cassian's Maven"
                url = uri("https://maven.cassian.cc")
            }
        }
        filter {
            includeGroupAndSubgroups("cc.cassian")
        }
    }
    maven {
        name = "Sisby Maven"
        url = uri("https://repo.sleeping.town/")
        content {
            includeGroup("folk.sisby")
        }
    }
    flatDir {
        dirs("libs")
    }
}

neoForge {
    version = property("deps.neoforge") as String
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        configureEach {
            systemProperty("neoforge.warnings.onlyin.hide", "true")
        }
        register("client") {
            gameDirectory = file("run/")
            client()
        }
        register("server") {
            gameDirectory = file("run/")
            server()
        }
    }

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets["main"])
        }
    }
}

dependencies {

    implementation("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    jarJar("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    "additionalRuntimeClasspath"("folk.sisby:kaleido-config:${property("deps.kaleido")}")

    compileOnly("dev.emi:emi-neoforge:${property("deps.emi")}+${property("deps.minecraft")}:api")
    runtimeOnly("dev.emi:emi-neoforge:${property("deps.emi")}+${property("deps.minecraft")}")


    compileOnly("maven.modrinth:farmers-delight:${property("deps.fd")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "me.shedaniel")
    }

    compileOnly("maven.modrinth:create-deco:${property("deps.create_deco")}")
    compileOnly("maven.modrinth:backported-spears:${property("deps.spears")}")

    compileOnly("maven.modrinth:always-a-bigger-fish:${property("deps.bigger_fish")}")

    // Create
    compileOnly("com.simibubi.create:create-${property("deps.minecraft")}:${property("deps.create")}:slim") { isTransitive = false }
    compileOnly("net.createmod.ponder:ponder-neoforge:${property("deps.ponder")}+mc${property("deps.minecraft")}")
    compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${property("deps.minecraft")}:${property("deps.flywheel")}")
    runtimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${property("deps.minecraft")}:${property("deps.flywheel")}")
    compileOnly("com.tterrag.registrate:Registrate:${property("deps.registrate")}")
}


tasks {
    processResources {
        exclude("**/fabric.mod.json", "infinity-forge.mixins.json", "**/*.accesswidener", "**/mods.toml")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">26")) {
        JavaVersion.VERSION_25
    } else {
        JavaVersion.VERSION_21
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    type = BETA
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version} NeoForge"
    version = "${property("mod.version")}+${property("deps.minecraft")}-neoforge"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("neoforge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        optional("mcqoy")
        optional("emi")
        optional("create")
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        optional("emi")
        optional("mcqoy")
    }
}