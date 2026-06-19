package com.molybdenum.alloyed.common.content.recipes;

import com.molybdenum.alloyed.common.handler.RecipeWrapper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public abstract class AbstractForgingRecipe implements Recipe<RecipeWrapper> {

	private final ItemStack output;
	private final int cookTime;

	public AbstractForgingRecipe(ItemStack output, int cookTime) {
		this.output = output;
		this.cookTime = cookTime;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	public ItemStack getResultItem() {
		return output.copy();
	}

	@Override
	public boolean showNotification() {
		return false;
	}
}