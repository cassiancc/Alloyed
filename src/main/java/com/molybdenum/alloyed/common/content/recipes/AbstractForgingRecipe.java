package com.molybdenum.alloyed.common.content.recipes;

import com.molybdenum.alloyed.common.handler.RecipeWrapper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public abstract class AbstractForgingRecipe implements Recipe<RecipeWrapper> {

	private final ItemStackTemplate output;
	private final int cookTime;

	public AbstractForgingRecipe(ItemStackTemplate output, int cookTime) {
		this.output = output;
		this.cookTime = cookTime;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	public ItemStack getResultItem() {
		return output.create();
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return null;
	}


	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public String group() {
		return "";
	}

	public abstract ItemStack assemble(RecipeWrapper inventory);
}