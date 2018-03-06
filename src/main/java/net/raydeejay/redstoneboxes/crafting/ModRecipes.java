package net.raydeejay.redstoneboxes.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import net.raydeejay.redstoneboxes.block.ModBlocks;
import net.raydeejay.redstoneboxes.items.ModItems;
import org.apache.logging.log4j.Level;

public class ModRecipes {
    public static void addRecipes() {
        // crafting ingredients
        GameRegistry.addRecipe(new ItemStack(ModItems.ironStick, 4),
                               "#", "#",
                               '#', Items.IRON_INGOT);

        GameRegistry.addRecipe(new ItemStack(ModItems.redstoneIndicator),
                               "##", "#.",
                               '#', Items.REDSTONE);

        // redstone box controls
        GameRegistry.addRecipe(new ItemStack(ModBlocks.radiusSelector),
                               "#.#", ".%.", "#.#",
                               '#', ModItems.redstoneIndicator,
                               '%', Blocks.STONE_BUTTON);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.rotationSelector),
                               ".#.", "#%#", ".#.",
                               '#', ModItems.redstoneIndicator,
                               '%', Blocks.STONE_BUTTON);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.coordinatesSelector),
                               "%#%", "#.#", "%#%",
                               '#', ModItems.redstoneIndicator,
                               '%', Blocks.STONE_BUTTON);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.randomizer),
                               "..#", "#%.", ".#.",
                               '#', ModItems.redstoneIndicator,
                               '%', Blocks.STONE_BUTTON);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.handbrake),
                               "#", "%", "#",
                               '#', ModItems.redstoneIndicator,
                               '%', Blocks.LEVER);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.courseSetter),
                               "#", "#", "%",
                               '#', ModItems.redstoneIndicator,
                               '%', Blocks.STONE_BUTTON);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.dimensionsSelector),
                               "#%#",
                               '#', ModItems.redstoneIndicator,
                               '%', Blocks.STONE_BUTTON);

        // multiblock components
        GameRegistry.addRecipe(new ItemStack(ModBlocks.redstoneChassis),
                               ".##", "#%#", "##.",
                               '#', ModItems.ironStick,
                               '%', Items.REDSTONE);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.abstractCore),
                               ".##", "#%#", "##.",
                               '#', ModItems.ironStick,
                               '%', Blocks.REDSTONE_BLOCK);

        // tools
        GameRegistry.addSmelting(new ItemStack(ModItems.ironStick),
                                 new ItemStack(ModItems.sonicRedstoneDriver),
                                 1.0f);

        RedstoneBoxesMod.logger.log(Level.INFO, "Registered Recipes");
    }
}