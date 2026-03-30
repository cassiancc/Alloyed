package com.molybdenum.alloyed.common.compat;

import cc.cassian.bigger_fish.items.BaitedRodItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BundleContents;

public class BiggerFishCompat {
	public static Item registerFishingRod(Item.Properties properties) {
		return new BaitedRodItem(properties.component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
	}
}
