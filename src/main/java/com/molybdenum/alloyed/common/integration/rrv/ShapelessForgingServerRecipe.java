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

import java.util.List;

public class ShapelessForgingServerRecipe implements ReliableServerRecipe {

    public static final ReliableServerRecipeType<ShapelessForgingServerRecipe> TYPE = ReliableServerRecipeType.register(
            Alloyed.asResource("forging"),
            () -> new ShapelessForgingServerRecipe(NonNullList.create(), null, 0)
    );
    private List<SlotContent> ingredients;
    private ItemStack resultItem;
    private int cookTime;

    public ShapelessForgingServerRecipe(NonNullList<Ingredient> ingredients, ItemStack resultItem, int cookTime) {
        this.ingredients = ingredients.stream().map(SlotContent::of).toList();
        this.resultItem = resultItem;
        this.cookTime = cookTime;
    }

    @Override
    public void writeToTag(CompoundTag tag) {
        tag.put("ingredients", TagUtil.writeList(this.ingredients, (origin, tag1) -> TagUtil.writeSlotContent(origin)));
        tag.put("result", TagUtil.encodeItemStackOnServer(resultItem));
        tag.putInt("cookingtime", cookTime);
    }

    @Override
    public void loadFromTag(CompoundTag tag) {
        this.ingredients = TagUtil.readList(tag, "ingredients", TagUtil::readSlotContent);
        this.resultItem = TagUtil.decodeItemStackOnServer(tag.getCompound("result").orElseGet(CompoundTag::new));
        this.cookTime = tag.getIntOr("cookingtime", 0);
    }

    @Override
    public ReliableServerRecipeType<? extends ReliableServerRecipe> getRecipeType() {
        return TYPE;
    }

    public List<SlotContent> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return resultItem;
    }

    public int getCookTime() {
        return cookTime;
    }
}
