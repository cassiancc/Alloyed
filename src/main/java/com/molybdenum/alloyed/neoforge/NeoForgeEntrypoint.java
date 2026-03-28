package com.molybdenum.alloyed.neoforge;
//? neoforge {
/*import com.molybdenum.alloyed.Alloyed;
import com.molybdenum.alloyed.AlloyedClient;
import com.molybdenum.alloyed.client.registry.ModSoundEvents;
import com.molybdenum.alloyed.client.screen.ForgeScreen;
import com.molybdenum.alloyed.common.CommonEventsHandler;
import com.molybdenum.alloyed.common.content.recipes.ModRecipes;
import com.molybdenum.alloyed.common.integration.rrv.AlloyedRRVPlugin;
import com.molybdenum.alloyed.common.item.ModCreativeModeTab;
import com.molybdenum.alloyed.common.registry.ModBlockEntities;
import com.molybdenum.alloyed.common.registry.ModBlockSetTypes;
import com.molybdenum.alloyed.common.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Alloyed.MOD_ID)
public class NeoForgeEntrypoint {
	public NeoForgeEntrypoint(IEventBus eventBus, ModContainer container) {

		NeoForgeMod.enableMilkFluid();
		if (FMLEnvironment.getDist().isClient()) {
			AlloyedClient.onClientInit();
			eventBus.addListener(NeoForgeEntrypoint::clientSetup);
		}
		eventBus.addListener(NeoForgeEntrypoint::commonSetup);
		eventBus.addListener(NeoForgeEntrypoint::onRegister);
		eventBus.addListener(NeoForgeEntrypoint::menuSetup);
		eventBus.addListener(NeoForgeEntrypoint::packSetup);
	}

	public static void onRegister(RegisterEvent event) {
		if (event.getRegistryKey().equals(Registries.BLOCK)) {
			ModBlockSetTypes.register();
			Alloyed.registerBlocks();
		} else if (event.getRegistryKey().equals(Registries.ITEM)) {
			Alloyed.registerItems();
			ModCreativeModeTab.register();
		} else if (event.getRegistryKey().equals(Registries.SOUND_EVENT)) {
			ModSoundEvents.register();
		} else if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
			ModBlockEntities.register();
		} else if (event.getRegistryKey().equals(Registries.RECIPE_TYPE)) {
			ModRecipes.register();
		} else if (event.getRegistryKey().equals(Registries.MENU)) {
			ModMenuTypes.register();
		}
	}

	public static void clientSetup(FMLClientSetupEvent event) {
		AlloyedClient.clientSetup();
	}

	public static void menuSetup(RegisterMenuScreensEvent event) {
		event.register(ModMenuTypes.FORGE_MENU.get(), ForgeScreen::new);
	}

	public static void commonSetup(FMLCommonSetupEvent event) {
		CommonEventsHandler.setupCommon();
	}

	public static void packSetup(AddPackFindersEvent event) {
		if (Alloyed.CONFIG.integratedForges)
			event.addPackFinders(Alloyed.asResource("resourcepacks/integrated_forges"), PackType.SERVER_DATA, Component.literal("Alloyed: Integrated Forges"), PackSource.FEATURE, true, Pack.Position.TOP);

	}

//	public static void addBlocks(final BlockEntityTypeAddBlocksEvent event) {
//		event.modify(AllBlockEntityTypes.ENCASED_COGWHEEL.getKey(), ModBlocks.STEEL_ENCASED_COGWHEEL.get(), ModBlocks.BRONZE_ENCASED_COGWHEEL.get());
//		event.modify(AllBlockEntityTypes.ENCASED_LARGE_COGWHEEL.getKey(), ModBlocks.STEEL_ENCASED_LARGE_COGWHEEL.get(),  ModBlocks.BRONZE_ENCASED_LARGE_COGWHEEL.get());
//		event.modify(AllBlockEntityTypes.ENCASED_SHAFT.getKey(), ModBlocks.STEEL_ENCASED_SHAFT.get(), ModBlocks.BRONZE_ENCASED_SHAFT.get());
//	}
}
*///?}