package com.molybdenum.alloyed.common;

import com.molybdenum.alloyed.Alloyed;
import com.molybdenum.alloyed.common.compat.farmersdelight.FarmersDelightCompat;
import com.molybdenum.alloyed.common.integration.ModCompat;
import com.molybdenum.alloyed.common.util.Platform;

import static com.molybdenum.alloyed.common.registry.ModBlocks.*;

public class CommonEventsHandler {

    // Common setup
    public static void setupCommon() {
        if (ModCompat.isFarmersDelightLoaded())
            FarmersDelightCompat.steelKnifeDispenseBehaviour();
    }
}
