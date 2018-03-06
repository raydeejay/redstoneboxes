package net.raydeejay.redstoneboxes.block.exterior;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import net.raydeejay.redstoneboxes.redstonebox.RedstoneBoxData;

import java.util.ArrayList;

public class AbstractCore extends Block implements ITileEntityProvider {
    public static final PropertyBool formed = PropertyBool.create("formed");
    private final String name = "abstract_core";

    public AbstractCore() {
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

    public boolean checkMultiblock(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if (playerIn == null) {
            return false;
        }

        ArrayList<BlockPos> chassis = new ArrayList<BlockPos>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (worldIn.getBlockState(pos.add(x, y, z)).getBlock() instanceof RedstoneChassis) {
                        //RedstoneBoxesMod.logger.log(Level.INFO, "found redstoneChassis");
                        chassis.add(pos.add(x, y, z));
                    }
                }
            }
        }

        // 26 redstoneChassis blocks in a 3x3x3 around the artron core will activate all of them
        // otherwise the redstoneChassis will be deactivated
        // since the core is in the middle, this allows for two redstonebox sitting next to each other
        boolean isFormed = chassis.size() == 26;

        for (BlockPos p : chassis) {
            if (isFormed) {
                worldIn.setBlockState(p, worldIn.getBlockState(p).withProperty(formed, true));
            } else {
                worldIn.setBlockState(p, worldIn.getBlockState(p).withProperty(formed, false));
            }
        }

        // TODO -probably needs better logic
        if (isFormed) {
            RedstoneBoxData data = RedstoneBoxData.get(worldIn);

            // TODO - store the position in which the player should appear when exiting through the door instead
            data.setExteriorPos(playerIn.getUniqueID(), playerIn.getPosition());
            data.setDimension(playerIn.getUniqueID(), playerIn.dimension);
        }

        return isFormed;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
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
//        return true;
//    }

}
