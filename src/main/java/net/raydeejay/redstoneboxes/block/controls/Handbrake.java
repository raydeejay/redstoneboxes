package net.raydeejay.redstoneboxes.block.controls;

import net.minecraft.block.BlockLever;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import net.raydeejay.redstoneboxes.redstonebox.RedstoneBox;
import net.raydeejay.redstoneboxes.redstonebox.RedstoneBoxData;
import net.raydeejay.redstoneboxes.tileentity.HandbrakeTileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class Handbrake extends BlockLever implements ITileEntityProvider {
    private final String name = "handbrake";

    public Handbrake() {
        super();
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName(RedstoneBoxesMod.MODID + "." + name);
        setRegistryName(new ResourceLocation(RedstoneBoxesMod.MODID, name));
        setHardness(2);
        setResistance(12);//slightly better than nether bricks (10)
        setHarvestLevel("pickaxe", 0);
        setLightLevel(0.5f);
        setDefaultState(this.blockState.getBaseState());
    }

    public String getName() {
        return name;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        // do the clicky thingie
        super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);

        if (worldIn.isRemote) {
            return true;
        }

        RedstoneBoxData data = RedstoneBoxData.get(worldIn);

        UUID uuid = playerIn.getUniqueID();
        if (data.isMaterialized(uuid)) {
            // fuel cost
            if (playerIn.experienceLevel < 1) {
                playerIn.addChatMessage(new TextComponentString("You need at least one level of experience"));
                return true;
            }

            playerIn.addExperienceLevel(-1);

            // remove redstoneChassis and core blocks
            BlockPos corepos = data.getExteriorPos(uuid);
            World extDim = DimensionManager.getWorld(data.getDimension(uuid));

            RedstoneBox.Dematerialize(extDim, corepos);

            // set to dematerialized
            playerIn.addChatMessage(new TextComponentString("The Redstone Box dematerializes!"));
            data.setMaterialized(uuid, false);

            // start travel
            // TODO sound should come from the tileentity
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            worldIn.playSound(null, x, y, z,
                              SoundEvents.ENTITY_MINECART_INSIDE,
                              SoundCategory.PLAYERS,
                              0.666F, 0.666F);
            // then loop the other minecart sound while still travelling?

            // (travelling takes time)
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof HandbrakeTileEntity) {
                ((HandbrakeTileEntity) te).startTravel();
            }

        } else {
            // TODO - only if it can finish travel
            BlockPos corepos = data.getTargetPos(uuid);
            World extDim = DimensionManager.getWorld(data.getDimension(uuid));

            // if the target position is not valid...
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof HandbrakeTileEntity) {
                if (((HandbrakeTileEntity) te).isTraveling()) {
                    playerIn.addChatMessage(new TextComponentString("The Redstone Box is still traveling!"));
                    return true;
                }
            }

            if (!RedstoneBox.canMaterializeAt(extDim, corepos, data.getRotation(uuid))) {
                // TODO - nudge it around (a max of N attempts)
                playerIn.addChatMessage(new TextComponentString("The Redstone Box cannot materialise there!"));
                return true;
            }

            // place core and redstoneChassis blocks
            RedstoneBox.Materialize(extDim, corepos, data.getRotation(uuid));

            // change the external position
            data.setExteriorPos(uuid, corepos);
            data.setMaterialized(uuid, true);

            // set to materialized
            playerIn.addChatMessage(new TextComponentString("The Redstone Box materializes!"));
            // play some sound? endermen something?
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new HandbrakeTileEntity();
    }
}
