package net.raydeejay.redstoneboxes.block.exterior;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;

public class BTCCube extends Block {
    public static final PropertyInteger appearance = PropertyInteger.create("appearance", 0, 15);
    private final String name = "btc_cube";

    public BTCCube() {
        super(Material.IRON);
        setSoundType(SoundType.STONE);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName(RedstoneBoxesMod.MODID + "." + name);
        setRegistryName(new ResourceLocation(RedstoneBoxesMod.MODID, name));
        setHardness(2);
        setLightLevel(0.5f);
        setBlockUnbreakable();
        setDefaultState(this.blockState.getBaseState().withProperty(appearance, 0));
    }

    public String getName() {
        return name;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, appearance);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(appearance);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(appearance, meta);
    }
}

//    @Override
//    public boolean onBlockActivated(World worldIn,
//                                    BlockPos pos,
//                                    IBlockState state,
//                                    EntityPlayer playerIn,
//                                    EnumHand hand,
//                                    @Nullable ItemStack heldItem,
//                                    EnumFacing side,
//                                    float hitX,
//                                    float hitY,
//                                    float hitZ) {
//        if (worldIn.isRemote) {
//            return true;
//        }
//        if (playerIn.isSneaking()) {
//            return false;
//        }
//
//        worldIn.setBlockState(pos, state.withProperty(appearance, (state.getValue(appearance) + 1) % 16));
//
//        return true;
//    }
