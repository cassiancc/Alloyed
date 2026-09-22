@file:Suppress("UnstableApiUsage")

plugins {
    id("fabric-loom")
    id("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
}

val minecraft = stonecutter.current.version
val accesswidener = "alloyed.accesswidener"
version = "${property("mod.version")}+${property("deps.minecraft")}"


tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = "$version"
        this["minecraft"] = prop("mod.mc_dep_fabric")
        this["aw_file"] = accesswidener
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
        expand(props)
    }
}

version = "$version-fabric"
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
        name = "Jitpack (DimLib)"
        url = uri("https://jitpack.io")
        content {
            includeGroupAndSubgroups("com.github.iPortalTeam")
            includeGroupAndSubgroups("com.github.qouteall")
            includeGroupAndSubgroups("com.github.Chocohead")

        }
    }
    maven {
        name = "Sisby Maven"
        url = uri("https://repo.sleeping.town/")
        content {
            includeGroupAndSubgroups("folk.sisby")
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
        name = "Fabricators of Create (Snapshots)"
        url = uri("https://mvn.devos.one/snapshots")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("io.github.fabricators_of_create")
            includeGroupAndSubgroups("com.simibubi")
            includeGroupAndSubgroups("com.tterrag")
            includeGroupAndSubgroups("io.github.tropheusj")
        }
    }
    maven {
        name = "Fabricators of Create (Releases)"
        url = uri("https://mvn.devos.one/releases")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("io.github.fabricators_of_create")
            includeGroupAndSubgroups("com.simibubi")
            includeGroupAndSubgroups("com.tterrag")
        }
    }
    maven {
        name = "Create, Ponder, Flywheel"
        url = uri("https://maven.createmod.net")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
        }
    }
    maven {
        name = "Fuzs Mod Resources"
        url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        content {
            includeGroupAndSubgroups("fuzs")
        }
    }
    maven {
        name = "reach-entity-attributes"
        url = uri("https://maven.jamieswhiteshirt.com/libs-release")
        content {
            includeGroupAndSubgroups("com.jamieswhiteshirt")
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
    flatDir {
        dirs("libs")
    }
    maven {
        name = "Xander Maven"
        url = uri("https://maven.isxander.dev/releases")
        content {
            includeGroupAndSubgroups("dev.isxander")
            includeGroupAndSubgroups("org.quiltmc.parsers")
        }
    }

}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    mappings(loom.layered {
        officialMojangMappings()
        if (hasProperty("deps.parchment"))
            parchment("org.parchmentmc.data:parchment-${property("deps.parchment")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    // Mod Menu
    implementation("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    include("folk.sisby:kaleido-config:${property("deps.kaleido")}")

    modImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")

    modImplementation("com.github.Chocohead:Fabric-ASM:${property("deps.fabric_asm")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "me.shedaniel")
    }


    //EMI
    if (hasProperty("deps.emi")) {
        modCompileOnly("dev.emi:emi-fabric:${property("deps.emi")}+${property("deps.minecraft")}:api")
        modLocalRuntime("dev.emi:emi-fabric:${property("deps.emi")}+${property("deps.minecraft")}")
    }

    modImplementation("maven.modrinth:farmers-delight-refabricated:${property("deps.fd")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "me.shedaniel")
    }

    modCompileOnly("maven.modrinth:create-deco:${property("deps.create_deco")}")
    modCompileOnly("maven.modrinth:backported-spears:${property("deps.spears")}")

    modCompileOnly("maven.modrinth:always-a-bigger-fish:${property("deps.bigger_fish")}")

    // Create
    modImplementation("com.simibubi.create:create-fucked-up-1.21.1:${property("deps.create")}") { isTransitive = false }
    modImplementation("net.createmod.ponder:Ponder-Fabric-${property("deps.minecraft")}:${property("deps.ponder")}")
    modImplementation("com.tterrag.registrate_fabric:Registrate-Fabric:${property("deps.registrate")}")
    modImplementation("dev.engine-room.flywheel:flywheel-fabric-${property("deps.minecraft")}:${property("deps.flywheel")}")


    val modules = listOf("base", "client_events", "mixin_extensions", "milk", "model_data", "model_loader", "models", "obj_loader", "recipe_book_categories", "tags")
    for (it in modules) modImplementation("io.github.fabricators_of_create.Porting-Lib:$it:"+property("deps.porting_lib"))

}


configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:${property("deps.fabric-loader")}")
        force("net.fabricmc:fabric-api:${property("deps.fabric_api")}")
    }
}


fabricApi {
    configureDataGeneration {
        outputDirectory = file("$rootDir/src/main/generated")
        client = true
    }
}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

tasks {
    processResources {
        exclude("**/neoforge.mods.toml", "infinity-forge.mixins.json", "**/mods.toml")
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


stonecutter {
    replacements.string {
        direction = eval(current.version, ">1.21.11")
        replace("ResourceLocation", "Identifier")
    }
    replacements.string {
        direction = eval(current.version, ">1.21.11")
        replace("simibubi", "zurrtum")
    }
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.remapJar.map { it.archiveFile.get() }

    type = BETA
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version} Fabric"
    version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("fabric")

    modrinth {
        additionalFile(tasks.remapSourcesJar) {
            type.set(SOURCES_JAR)
        }
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        optional("mcqoy")
        optional("emi")

    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        optional("mcqoy")
        optional("emi")
        client=true
        server=true
    }
}

loom {
    accessWidenerPath = rootProject.file("src/main/resources/$accesswidener")
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand(mapOf(
            // other properties
            "aw_file" to accesswidener,
        ))
    }
    from(rootProject.file("src/${minecraft}/resources")) {
        include("/*")
    }
}
