package com.molybdenum.alloyed.common.integration.rrv;

import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import com.molybdenum.alloyed.Alloyed;
import com.molybdenum.alloyed.common.registry.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgingClientRecipeType implements ReliableClientRecipeType {

    public static final ForgingClientRecipeType INSTANCE = new ForgingClientRecipeType();

    @Override
    public Component getDisplayName() {
        return Component.translatable("recipe.alloyed.forging");
    }

    @Override
    public int getDisplayWidth() {
        return 120;
    }

    @Override
    public int getDisplayHeight() {
        return 60;
    }

    @Override
    public @Nullable Identifier getGuiTexture() {
        return Alloyed.asResource("textures/gui/forge_gui_rrv.png");
    }

    @Override
    public int getSlotCount() {
        return 11;
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public void placeSlots(RecipeViewMenu.SlotDefinition builder) {
        //ingredients
        builder.addItemSlot(0, 2, 3);
        builder.addItemSlot(1, 20, 3);
        builder.addItemSlot(2, 38, 3);
        builder.addItemSlot(3, 2, 21);
        builder.addItemSlot(4, 20, 21);
        builder.addItemSlot(5, 38, 21);
        builder.addItemSlot(6, 2, 39);
        builder.addItemSlot(7, 20, 39);
        builder.addItemSlot(8, 38, 39);
        // fuel
        builder.addItemSlot(9, 66, 39);
        //output
        builder.addItemSlot(10, 94, 12);
    }

    @Override
    public Identifier getId() {
        return Alloyed.asResource("forging");
    }

    @Override
    public ItemStack getIcon() {
        return ModBlocks.FORGE.get().asItem().getDefaultInstance();
    }

    @Override
    public List<ItemStack> getCraftReferences() {
        return List.of(ModBlocks.FORGE.get().asItem().getDefaultInstance());
    }

}
