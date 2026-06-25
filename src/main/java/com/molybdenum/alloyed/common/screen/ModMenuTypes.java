package com.molybdenum.alloyed.common.screen;

import com.molybdenum.alloyed.common.CommonRegistry;
//? fabric
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
//? neoforge
/*import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;*/

import java.util.function.Supplier;

public class ModMenuTypes {
	//? fabric
	public static final Supplier<MenuType<ForgeMenu>> FORGE_MENU = CommonRegistry.registerMenu("forge", () -> new ExtendedScreenHandlerType<>(ForgeMenu::new, BlockPos.STREAM_CODEC));

	//? neoforge
	/*public static final Supplier<MenuType<ForgeMenu>> FORGE_MENU = CommonRegistry.registerMenu("forge", () -> IMenuTypeExtension.create((ForgeMenu::new)));*/

	public static void register() {

	}
}