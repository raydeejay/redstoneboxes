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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import net.raydeejay.redstoneboxes.redstonebox.RedstoneBoxData;

import javax.annotation.Nullable;

public class CourseSetter extends BlockButtonStone {
    private final String name = "course_setter";

    public CourseSetter() {
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
        if (data.isMaterialized(playerIn.getUniqueID())) {
            playerIn.addChatMessage(new TextComponentString("The TARDIS is materialized!"));
            return true;
        }

        // set the target coordinates from the course coordinates
        // TODO - height should be determined by the ground level more or less?
        // right now it lands at the surface always
        // extend platform when landing on air...

        BlockPos extPos = data.getCoursePos(playerIn.getUniqueID());
        int y = 250;

        BlockPos adjPos = new BlockPos(extPos.getX(), y, extPos.getZ());

        World overworld = DimensionManager.getWorld(data.getDimension(playerIn.getUniqueID()));
        while (adjPos.getY() > 5 && !overworld.getBlockState(adjPos).isFullBlock()) {
            adjPos = adjPos.add(0, -1, 0);
        }

        // pull 2 up and we have the core position
        adjPos = adjPos.add(0, 2, 0);

        playerIn.addChatMessage(new TextComponentString(String.format("moving to X: %d  Y: %d  Z: %d",
                                                                      adjPos.getX(), adjPos.getY(), adjPos.getZ())));
        data.setTargetPos(playerIn.getUniqueID(), adjPos);

        return true;
    }
}