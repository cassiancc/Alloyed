package com.molybdenum.alloyed.common.screen.slot;

import com.molybdenum.alloyed.common.content.blocks.entities.ForgeBlockEntity;
import com.molybdenum.alloyed.common.handler.ItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FuelValues;

import static net.minecraft.world.inventory.FurnaceFuelSlot.isBucket;

public class ModFuelSlot extends ModInputSlot {
	private final FuelValues fuelValues;

	public ModFuelSlot(ItemHandler itemHandler, FuelValues fuelValues, int index, int x, int y) {
		super(itemHandler, index, x, y);
		this.fuelValues = fuelValues;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return ForgeBlockEntity.isFuel(fuelValues, stack)  || isBucket(stack);
	}
}