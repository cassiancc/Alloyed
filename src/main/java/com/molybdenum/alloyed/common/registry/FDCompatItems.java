package com.molybdenum.alloyed.common.registry;

import com.molybdenum.alloyed.Alloyed;
import com.molybdenum.alloyed.common.compat.farmersdelight.FarmersDelightCompat;
import com.molybdenum.alloyed.common.integration.ModCompat;
import net.minecraft.world.item.Item;

import java.util.List;

public class FDCompatItems {
    public static final ItemEntry<Item> STEEL_KNIFE = ModItems.registerItem(
            "steel_knife",
            properties -> ModCompat.isFarmersDelightLoaded() ?
                    FarmersDelightCompat.newSteelKnife(properties) :
                    new Item(properties.stacksTo(1)),
            new Item.Properties(),
            !ModCompat.isFarmersDelightLoaded()
    );

    public static void register() {
        Alloyed.LOGGER.debug("Registering ModCompatItems!");
    }

    public static List<ItemEntry<?>> getFDItems() {
        return List.of(STEEL_KNIFE);
    }
}
