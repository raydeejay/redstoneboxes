package net.raydeejay.redstoneboxes.block.controls;

import net.minecraft.block.BlockButtonStone;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import net.raydeejay.redstoneboxes.redstonebox.RedstoneBoxData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DimensionSelector extends BlockButtonStone {
    private final String name = "dimension_selector";

    public DimensionSelector() {
        super();
        setSoundType(SoundType.STONE);
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
        // regular button behaviour
        super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);

        if (worldIn.isRemote) {
            return true;
        }

        if (playerIn.isSneaking()) {
            return false;
        }

        RedstoneBoxData data = RedstoneBoxData.get(worldIn);
        UUID uuid = playerIn.getUniqueID();

        if (data.isMaterialized(uuid)) {
            playerIn.addChatMessage(new TextComponentString("The TARDIS is materialized!"));
            return true;
        }

        // cycle through available dimensions
        // forbid travel to redstonebox dimension (maybe make it configurable when the internals don't conflict with it)

        // get a list of dimensions
        List<Integer> ids = new ArrayList();

        for (DimensionType t : DimensionType.values()) {
            for (int id : DimensionManager.getDimensions(t)) {
                ids.add(id);
            }
        }

        // find the next one to cycle to
        Collections.sort(ids);
        int currentDimIdx = ids.indexOf(data.getDimension(uuid));
        int nextDimIdx = (currentDimIdx + 1) % ids.size();

        // get its name, if it has one
        String dimName = "";

        try {
            dimName = DimensionManager.getWorld(ids.get(nextDimIdx)).provider.getDimensionType().getName();
        } catch (Exception e) {
            dimName = String.format("Exception getting name: %s", e.getMessage());
        }

        data.setDimension(uuid, ids.get(nextDimIdx));
        playerIn.addChatMessage(new TextComponentString(String.format("Dimension set to %d (%s)",
                                                                      ids.get(nextDimIdx),
                                                                      dimName)));
//        playerIn.addChatMessage(new TextComponentString(String.format("Dimension set to %d", ids.get(nextDimIdx))));

        return true;
    }
}