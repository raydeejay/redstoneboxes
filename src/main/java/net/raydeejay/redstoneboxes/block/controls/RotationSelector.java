package net.raydeejay.redstoneboxes.block.controls;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
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

public class RotationSelector extends Block {
    public static final PropertyDirection facing = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private final String name = "rotation_selector";

    public RotationSelector() {
        super(Material.ROCK);
        setSoundType(SoundType.STONE);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName(RedstoneBoxesMod.MODID + "." + name);
        setRegistryName(new ResourceLocation(RedstoneBoxesMod.MODID, name));
        setHardness(2);
        setResistance(12);
        setHarvestLevel("pickaxe", 0);
        setLightLevel(0.5f);
        setDefaultState(this.blockState.getBaseState().withProperty(facing, EnumFacing.NORTH));
    }

    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    }

    void adjustFacing(World worldIn, EntityPlayer playerIn, IBlockState newState) {
        UUID uuid = playerIn.getUniqueID();

        RedstoneBoxData data = RedstoneBoxData.get(worldIn);
        data.setRotation(uuid, newState.getValue(facing).getIndex());

        playerIn.addChatMessage(new TextComponentString(String.format("rotation set to %s",
                                                                      newState.getValue(facing).toString())));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(facing).getHorizontalIndex();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(facing, EnumFacing.getHorizontal(meta));
    }

    protected IBlockState cycleProperty(World worldIn, BlockPos pos, IBlockState state) {
        IBlockState newState = state.cycleProperty(facing);
        worldIn.setBlockState(pos, newState);
        return newState;
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

        // cycle property
        IBlockState newState = cycleProperty(worldIn, pos, state);

        // adjust setting
        adjustFacing(worldIn, playerIn, newState);

        return true;
    }
}