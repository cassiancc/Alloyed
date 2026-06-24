package com.molybdenum.alloyed.common.item;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import com.molybdenum.alloyed.Alloyed;
import com.molybdenum.alloyed.common.registry.ModItems;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class ModArmourMaterials {

    public static final Holder<ArmorMaterial> STEEL = register(
            "steel",
            new int[] { 3, 7, 5, 3, 7},
            10,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            1.0F,
            0.1F,
            ()->Ingredient.of(ModItems.STEEL_INGOT.get())
    );

    private static Holder<ArmorMaterial> register(
            String name,
            int[] defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(Alloyed.asResource(name)));
        return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(
            String name,
            int[] defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngridient,
            List<ArmorMaterial.Layer> layers
    ) {
        EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
            enummap.put(armoritem$type, defense[armoritem$type.ordinal()]);
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, Alloyed.asResource(name),
                new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngridient, layers, toughness, knockbackResistance)
        );
    }

    public static void register() {

    }
}