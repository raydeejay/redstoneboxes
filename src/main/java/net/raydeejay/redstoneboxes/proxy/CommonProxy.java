package net.raydeejay.redstoneboxes.proxy;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.raydeejay.redstoneboxes.Config;
import net.raydeejay.redstoneboxes.worldgen.ModDimensions;
import net.raydeejay.redstoneboxes.crafting.ModRecipes;

import java.io.File;

public class CommonProxy {
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "redstonebox.cfg"));
        Config.readConfig();
        // Initialization of blocks and items typically goes here:
        // ModBlocks.init();
        // ModItems.init();
        // ModCrafting.init();
        ModRecipes.addRecipes();
        ModDimensions.init();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
