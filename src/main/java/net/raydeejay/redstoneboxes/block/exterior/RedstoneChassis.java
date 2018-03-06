package net.raydeejay.redstoneboxes.block.exterior;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import org.apache.logging.log4j.Level;

public class RedstoneChassis extends Block {
    public static final PropertyBool formed = PropertyBool.create("formed");
    private final String name = "redstone_chassis";

    public RedstoneChassis() {
        super(Material.IRON);
        setSoundType(SoundType.STONE);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName(RedstoneBoxesMod.MODID + "." + name);
        setRegistryName(new ResourceLocation(RedstoneBoxesMod.MODID, name));
        setHardness(2);
        setResistance(12);//slightly better than nether bricks (10)
        setHarvestLevel("pickaxe", 0);
        setLightLevel(0.5f);
        setDefaultState(this.blockState.getBaseState().withProperty(formed, false));
    }

    public String getName() {
        return name;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, formed);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(formed) ? 1 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(formed, meta == 1 ? true : false);
    }

    // hmmmm
    public BlockPos getCore(World worldIn, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos cpos = pos.add(x, y, z);
                    if (worldIn.getBlockState(cpos).getBlock() instanceof AbstractCore) {
                        return cpos;
                    }
                }
            }
        }

        return null;
    }

    public boolean checkMultiblock(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn) {
        BlockPos cpos = this.getCore(worldIn, pos);

        if (cpos == null) {
            return false;
        }

        IBlockState corestate = worldIn.getBlockState(cpos);
        AbstractCore coreblock = (AbstractCore) corestate.getBlock();

        return coreblock.checkMultiblock(corestate, worldIn, cpos, playerIn);
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
                                EntityLivingBase placer, ItemStack stack) {
        if (!(placer instanceof EntityPlayer)) {
            return;
        }

        BlockPos corepos = this.getCore(worldIn, pos);

        if (corepos == null) {
            return;
        }

        IBlockState corestate = worldIn.getBlockState(corepos);
        AbstractCore coreblock = (AbstractCore) corestate.getBlock();

        RedstoneBoxesMod.logger.log(Level.INFO, "found core, checking redstoneChassis");
        coreblock.checkMultiblock(corestate, worldIn, corepos, (EntityPlayer) placer);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        RedstoneBoxesMod.logger.log(Level.INFO, "removed a piece of redstoneChassis, checking multiblock");
        BlockPos corepos = this.getCore(worldIn, pos);

        if (corepos == null) {
            return;
        }

        IBlockState corestate = worldIn.getBlockState(corepos);
        AbstractCore coreblock = (AbstractCore) corestate.getBlock();

        RedstoneBoxesMod.logger.log(Level.INFO, "found core, checking redstoneChassis");
        coreblock.checkMultiblock(corestate, worldIn, corepos, null);
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
//        if (worldIn.isRemote) { return true; }
//        if (playerIn.isSneaking()) { return false; }
//
//        worldIn.setBlockState(pos, state.withProperty(formed, (state.getValue(formed) + 1) % 11));
//
//        return true;
//    }
