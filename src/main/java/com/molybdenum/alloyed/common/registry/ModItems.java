package com.molybdenum.alloyed.common.registry;

import com.molybdenum.alloyed.Alloyed;
import com.molybdenum.alloyed.common.compat.SpearsCompat;
import com.molybdenum.alloyed.common.compat.BiggerFishCompat;
import com.molybdenum.alloyed.common.item.ModArmourMaterials;
import com.molybdenum.alloyed.common.item.ModItemTiers;
import com.molybdenum.alloyed.common.util.Platform;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;

import java.util.ArrayList;
import java.util.function.Function;

import static com.molybdenum.alloyed.Alloyed.MOD_ID;

public class ModItems {
    public static ArrayList<Item> ITEMS = new ArrayList<>();

    // Ingots

    public static final ItemEntry<Item> BRONZE_INGOT = registerItem(
            "bronze_ingot"
    );

    public static final ItemEntry<Item> STEEL_INGOT = registerItem(
            "steel_ingot"
    );

    // Nuggets

    public static final ItemEntry<Item> BRONZE_NUGGET = registerItem(
            "bronze_nugget"
    );

    public static final ItemEntry<Item> STEEL_NUGGET = registerItem(
            "steel_nugget"
    );

    // Sheets

    public static final ItemEntry<Item> BRONZE_SHEET = registerItem("bronze_sheet");

    public static final ItemEntry<Item> STEEL_SHEET = registerItem("steel_sheet");

    // Steel toolset.

    public static final ItemEntry<Item> STEEL_SWORD = registerItem(
            "steel_sword",
            properties -> new SwordItem(ModItemTiers.STEEL, properties.attributes(SwordItem.createAttributes(ModItemTiers.STEEL, 3, -2.4F)))
    );

    public static final ItemEntry<Item> STEEL_SPEAR = registerSpear();

	private static ItemEntry<Item> registerSpear() {
        if (Platform.isLoaded("spears")) {
            ItemEntry<Item> steelSpear = new ItemEntry<>(Alloyed.asResource("steel_spear"), SpearsCompat.registerSpear());
            ITEMS.add(steelSpear.asItem());
            return steelSpear;
        } else return registerItem("steel_spear", Item::new, new Item.Properties(), true);
	}

    public static final ItemEntry<Item> STEEL_PICKAXE = registerItem(
            "steel_pickaxe",
            properties -> new PickaxeItem(ModItemTiers.STEEL, properties.attributes(PickaxeItem.createAttributes(ModItemTiers.STEEL, 1, -2.8F)))
    );

    public static final ItemEntry<Item> STEEL_AXE = registerItem(
            "steel_axe",
            properties -> new AxeItem(ModItemTiers.STEEL, properties.attributes(AxeItem.createAttributes(ModItemTiers.STEEL, 5.0F, -3.0F)))
    );


    public static final ItemEntry<Item> STEEL_SHOVEL = registerItem(
            "steel_shovel",
            properties -> new ShovelItem(ModItemTiers.STEEL, properties.attributes(ShovelItem.createAttributes(ModItemTiers.STEEL, 1.5F, -3.0F)))
    );


    public static final ItemEntry<Item> STEEL_HOE = registerItem(
            "steel_hoe",
            properties -> new HoeItem(ModItemTiers.STEEL, properties.attributes(HoeItem.createAttributes(ModItemTiers.STEEL, -3, 0.0F)))
    );


    public static final ItemEntry<ShearsItem> STEEL_SHEARS = registerItem("steel_shears", properties -> new ShearsItem(properties.component(DataComponents.TOOL, ShearsItem.createToolProperties()).durability(750)));


    public static final ItemEntry<Item> STEEL_FISHING_ROD = registerItem("steel_fishing_rod", properties -> registerFishingRod(properties.stacksTo(1)));

    // Steel Armour
    public static final ItemEntry<Item> STEEL_HELMET = registerItem("steel_helmet", properties -> new ArmorItem(ModArmourMaterials.STEEL, ArmorItem.Type.HELMET, properties.durability(330)));

    public static final ItemEntry<Item> STEEL_CHESTPLATE = registerItem("steel_chestplate", properties -> new ArmorItem(ModArmourMaterials.STEEL, ArmorItem.Type.CHESTPLATE, properties.durability(480)));

    public static final ItemEntry<Item> STEEL_LEGGINGS = registerItem("steel_leggings", properties -> new ArmorItem(ModArmourMaterials.STEEL, ArmorItem.Type.LEGGINGS, properties.durability(450)));

    public static final ItemEntry<Item> STEEL_BOOTS = registerItem("steel_boots", properties -> new ArmorItem(ModArmourMaterials.STEEL, ArmorItem.Type.BOOTS, properties.durability(390)));

    public static final ItemEntry<Item> STEEL_HORSE_ARMOR = registerItem("steel_horse_armor", properties -> new AnimalArmorItem(ModArmourMaterials.STEEL, AnimalArmorItem.BodyType.EQUESTRIAN, false, properties.stacksTo(1)));

    public static final ItemEntry<Item> STEEL_NAUTILUS_ARMOR = registerItem("steel_nautilus_armor", properties -> new Item(properties.stacksTo(1)), new Item.Properties(), true);

    // End Item Entries

    public static void register() {
        Alloyed.LOGGER.debug("Registering ModItems!");
    }

    private static ItemEntry<Item> registerItem(String name) {
        return registerItem(name, Item::new);
    }

    protected static <T extends Item> ItemEntry<T> registerItem(String id, Function<Item.Properties, T> factory, Item.Properties settings, boolean hidden) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, id));
        T block = factory.apply(settings);
        var entry = Registry.register(BuiltInRegistries.ITEM, key, block);
        if (!hidden)
            ITEMS.add(entry);
        return new ItemEntry<>(key.location(), entry);
    }

    private static <T extends Item> ItemEntry<T> registerItem(String id, Function<Item.Properties, T> factory, Item.Properties settings) {
        return registerItem(id, factory, settings, false);
    }

    public static ItemEntry<?> registerBlockItem(BlockEntry<?> blockEntry, boolean hideFromCreative) {
        return registerItem(blockEntry.getId().getPath(), (properties)-> new BlockItem(blockEntry.get(), properties), new Item.Properties(), hideFromCreative);
    }

    private static <T extends Item> ItemEntry<T> registerItem(String id, Function<Item.Properties, T> factory) {
        return registerItem(id, factory, new Item.Properties());
    }

    private static Item registerFishingRod(Item.Properties properties) {
        if (Platform.isLoaded("bigger_fish"))
            return BiggerFishCompat.registerFishingRod(properties);
        return new FishingRodItem(properties.durability(512));
    }
}