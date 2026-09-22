package com.molybdenum.alloyed.common.util;

//? fabric {
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.loader.api.FabricLoader;
//?} else {
/*import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;
*///?}
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;


import java.nio.file.Path;
import java.util.function.Supplier;

public class Platform {
	public static boolean isLoaded(String mod) {
		//? fabric
		return FabricLoader.getInstance().isModLoaded(mod);
		//? neoforge
		/*return LoadingModList.get().getModFileById(mod) != null;*/
	}

	public static void addWaxable(Block block, Block waxedBlock) {
		//? fabric
		OxidizableBlocksRegistry.registerWaxable(block, waxedBlock);
	}

	public static void addWeathering(Block block, Block waxedBlock) {
		//? fabric
		OxidizableBlocksRegistry.registerNextStage(block, waxedBlock);
	}

	public static Path getConfigDir() {
		//? fabric
		return FabricLoader.getInstance().getConfigDir();
		//? neoforge
		/*return FMLPaths.CONFIGDIR.get();*/
	}
}
