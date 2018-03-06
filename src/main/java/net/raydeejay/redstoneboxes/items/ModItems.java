package net.raydeejay.redstoneboxes.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static SonicRedstoneDriver sonicRedstoneDriver;
    public static IronStick ironStick;
    public static RedstoneIndicator redstoneIndicator;

    public static final Item[] ITEMS = new Item[]{
            sonicRedstoneDriver = new SonicRedstoneDriver(),
            ironStick = new IronStick(),
            redstoneIndicator = new RedstoneIndicator()
    };

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        sonicRedstoneDriver.initModel();
        ironStick.initModel();
        redstoneIndicator.initModel();
    }
}
