package net.raydeejay.redstoneboxes.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;

public class IronStick extends Item {
    private final String name = "iron_stick";

    public IronStick() {
        setUnlocalizedName(RedstoneBoxesMod.MODID + "." + name);
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(new ResourceLocation(RedstoneBoxesMod.MODID, name));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0,
                                                   new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
