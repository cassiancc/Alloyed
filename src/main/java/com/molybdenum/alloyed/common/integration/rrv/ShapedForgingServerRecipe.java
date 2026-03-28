package com.molybdenum.alloyed.common.integration.rrv;

import cc.cassian.rrv.api.TagUtil;
import cc.cassian.rrv.api.recipe.ReliableServerRecipe;
import cc.cassian.rrv.api.recipe.ReliableServerRecipeType;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import com.molybdenum.alloyed.Alloyed;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.List;

public class ShapedForgingServerRecipe implements ReliableServerRecipe {

    public static final ReliableServerRecipeType<ShapedForgingServerRecipe> TYPE = ReliableServerRecipeType.register(
            Alloyed.asResource("forging_shaped"),
            () -> new ShapedForgingServerRecipe(0, 0, null, null, 0)
    );
	private int width;
	private int height;
	private HashMap<Integer, SlotContent> ingredients;
    private ItemStack resultItem;
    private int cookTime;

    public ShapedForgingServerRecipe(int width, int height, HashMap<Integer, SlotContent> ingredients, ItemStack resultItem, int cookTime) {
		this.width = width;
		this.height = height;
		this.ingredients = ingredients;
        this.resultItem = resultItem;
        this.cookTime = cookTime;
    }

    @Override
    public void writeToTag(CompoundTag tag) {
        tag.putInt("width", this.width);
        tag.putInt("height", this.height);
        this.ingredients.forEach((slotId, ingredient) -> tag.put("ci_" + slotId, TagUtil.writeSlotContent(ingredient)));
        tag.put("result", TagUtil.encodeItemStackOnServer(resultItem));
        tag.putInt("cookingtime", cookTime);
    }

    @Override
    public void loadFromTag(CompoundTag tag) {
        this.width = tag.getIntOr("width", 0);
        this.height = tag.getIntOr("height", 0);
        HashMap<Integer, SlotContent> ingredients = new HashMap<>();
        tag.keySet().forEach((key) -> {
            if (key.startsWith("ci_")) {
                int slot = Integer.parseInt(key.replace("ci_", ""));
                ingredients.put(slot, TagUtil.readSlotContent(tag.getCompound(key).orElseGet(CompoundTag::new)));
            }
        });
        this.ingredients = ingredients;
        this.resultItem = TagUtil.decodeItemStackOnServer(tag.getCompound("result").orElseGet(CompoundTag::new));
        this.cookTime = tag.getIntOr("cookingtime", 0);
    }

    @Override
    public ReliableServerRecipeType<? extends ReliableServerRecipe> getRecipeType() {
        return TYPE;
    }

    public HashMap<Integer, SlotContent> getIngredients() {
        return this.ingredients;
    }

    public ItemStack getResult() {
        return resultItem;
    }

    public int getCookTime() {
        return cookTime;
    }

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
