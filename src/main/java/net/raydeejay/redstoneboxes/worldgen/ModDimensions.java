package net.raydeejay.redstoneboxes.worldgen;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.raydeejay.redstoneboxes.Config;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;

public class ModDimensions {
    public static DimensionType redboxDimensionType;

    public static void init() {
        registerDimensionTypes();
        registerDimensions();
    }

    private static void registerDimensions() {
        DimensionManager.registerDimension(Config.dimensionId, redboxDimensionType);
    }

    private static void registerDimensionTypes() {
        redboxDimensionType = DimensionType.register(RedstoneBoxesMod.MODID, "_redbox", Config.dimensionId,
                                                     RedboxWorldProvider.class, false);
    }
}
