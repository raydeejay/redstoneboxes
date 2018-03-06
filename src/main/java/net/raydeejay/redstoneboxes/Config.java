package net.raydeejay.redstoneboxes;

import net.minecraftforge.common.config.Configuration;
import net.raydeejay.redstoneboxes.proxy.CommonProxy;
import org.apache.logging.log4j.Level;

public class Config {
    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_DIMENSIONS = "dimensions";

    public static int dimensionId = 4269;

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
            initDimensionConfig(cfg);
        } catch (Exception e1) {
            RedstoneBoxesMod.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
    }

    private static void initDimensionConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS, "Dimension configuration");
        dimensionId = cfg.getInt("redboxDim", CATEGORY_GENERAL, dimensionId, 2, 32000,
                                 "Set the Redstone Boxes interiors dimension ID here");

    }
}
