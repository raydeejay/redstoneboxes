package net.raydeejay.redstoneboxes.block.controls;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import net.raydeejay.redstoneboxes.redstonebox.RedstoneBoxData;

import javax.annotation.Nullable;
import java.util.UUID;

public class CoordinatesSelector extends Block {
    private final String name = "coordinates_selector";

    public CoordinatesSelector() {
        super(Material.IRON);
        setSoundType(SoundType.STONE);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName(RedstoneBoxesMod.MODID + "." + name);
        setRegistryName(new ResourceLocation(RedstoneBoxesMod.MODID, name));
        setHardness(2);
        setResistance(12);//slightly better than nether bricks (10)
        setHarvestLevel("pickaxe", 0);
        setLightLevel(0.5f);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    }

    @Override
    public boolean onBlockActivated(World worldIn,
                                    BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    @Nullable ItemStack heldItem,
                                    EnumFacing side,
                                    float hitX,
                                    float hitY,
                                    float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        if (playerIn.isSneaking()) {
            return false;
        }

        UUID uuid = playerIn.getUniqueID();
        RedstoneBoxData data = RedstoneBoxData.get(worldIn);

        int radius = data.getRadius(uuid);
        int x = 0;
        int y = 0;
        int z = 0;

        // TODO - take orientation into account
        if (hitX > 0.25 && hitX < 0.75) {
            if (hitZ < 0.25) {
                --z;
            }
            if (hitZ > 0.75) {
                ++z;
            }
        }

        if (hitZ > 0.25 && hitZ < 0.75) {
            if (hitX < 0.25) {
                --x;
            }
            if (hitX > 0.75) {
                ++x;
            }
        }

        BlockPos newCourse = data.getCoursePos(uuid).add(x * radius, y, z * radius);
        data.setCoursePos(uuid, newCourse);
        playerIn.addChatMessage(new TextComponentString(String.format("X %d Y %d Z %d",
                                                                      newCourse.getX(),
                                                                      newCourse.getY(),
                                                                      newCourse.getZ())));

        return true;
    }
}
