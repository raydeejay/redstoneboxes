package net.raydeejay.redstoneboxes;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.raydeejay.redstoneboxes.block.ModBlocks;
import net.raydeejay.redstoneboxes.items.ModItems;
import net.raydeejay.redstoneboxes.tileentity.HandbrakeTileEntity;

@Mod.EventBusSubscriber
public class RegistryEventHandler {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS);

        //RedstoneBoxesMod.logger.log(Level.INFO, "Registered blocks");

        GameRegistry.registerTileEntity(HandbrakeTileEntity.class, RedstoneBoxesMod.MODID + ".handbrake_entity");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
         event.getRegistry().registerAll(ModItems.ITEMS);

        for (Block block : ModBlocks.BLOCKS) {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }

        //RedstoneBoxesMod.logger.log(Level.INFO, "Registered items");
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Block block : ModBlocks.BLOCKS) {
            ModelResourceLocation loc = new ModelResourceLocation(block.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),
                                                       0,
                                                       loc);
        }

        for (Item item : ModItems.ITEMS) {
            ModelLoader.setCustomModelResourceLocation(item,
                                                       0,
                                                       new ModelResourceLocation(item.getRegistryName(),
                                                                                 "inventory"));
        }

        //RedstoneBoxesMod.logger.log(Level.INFO, "Registered models");
    }

}
