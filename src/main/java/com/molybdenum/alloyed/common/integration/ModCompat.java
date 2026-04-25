package com.molybdenum.alloyed.common.integration;

import com.molybdenum.alloyed.common.util.Platform;

public class ModCompat {
	public static boolean isFarmersDelightLoaded() {
		return Platform.isLoaded("farmersdelight");
	}
}
