package com.molybdenum.alloyed.common.compat.create;

import com.molybdenum.alloyed.client.ponder.AlloyedPonderPlugin;
import com.molybdenum.alloyed.common.content.blocks.WeatheringBronzePillarBlock;
import com.molybdenum.alloyed.common.registry.BlockEntry;
import com.molybdenum.alloyed.common.registry.ModBlocks;
import com.molybdenum.alloyed.common.util.Platform;
import com.zurrtum.create.client.ponder.foundation.PonderIndex;
import com.zurrtum.create.content.decoration.palettes.ConnectedPillarBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.List;

import static com.molybdenum.alloyed.common.registry.ModBlocks.registerBlock;

public class CreateCompat {
	public static void registerPonders() {
		PonderIndex.addPlugin(new AlloyedPonderPlugin());
	}

	public static List<BlockEntry<? extends Block>> registerBronzePillarSet(String id, WeatheringCopper.WeatherState state) {
		var block = registerBlock(id, (properties)-> new WeatheringBronzePillarBlock(state, properties), ModBlocks.bronzeProperties());
		var waxedBlock = registerBlock("waxed_"+id, ConnectedPillarBlock::new, ModBlocks.bronzeProperties());
		ModBlocks.addWeathering(id, state, block);
		Platform.addWaxable(block.get(), waxedBlock.get());
		return List.of(block, waxedBlock);
	}
}
