package com.molybdenum.alloyed.common.screen.slot;

import com.molybdenum.alloyed.common.content.blocks.entities.ForgeBlockEntity;
import com.molybdenum.alloyed.common.handler.ItemHandler;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.inventory.FurnaceFuelSlot.isBucket;

public class ModFuelSlot extends ModInputSlot {

	public ModFuelSlot(ItemHandler itemHandler, int index, int x, int y) {
		super(itemHandler, index, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return ForgeBlockEntity.isFuel(null, stack)  || isBucket(stack);
	}
}