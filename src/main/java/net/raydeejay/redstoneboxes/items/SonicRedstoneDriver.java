package net.raydeejay.redstoneboxes.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.raydeejay.redstoneboxes.Config;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import net.raydeejay.redstoneboxes.Utils;
import net.raydeejay.redstoneboxes.block.exterior.AbstractCore;
import net.raydeejay.redstoneboxes.block.exterior.BTCCube;
import net.raydeejay.redstoneboxes.block.exterior.RedstoneChassis;
import net.raydeejay.redstoneboxes.commands.CustomTeleporter;
import net.raydeejay.redstoneboxes.redstonebox.RedstoneBox;
import net.raydeejay.redstoneboxes.redstonebox.RedstoneBoxData;

import java.util.List;
import java.util.UUID;

import static net.raydeejay.redstoneboxes.block.exterior.BTCCube.appearance;
import static net.raydeejay.redstoneboxes.items.SonicRedstoneDriver.Modes.*;

public class SonicRedstoneDriver extends Item {
    private final String name = "sonic_redstone_driver";

    public SonicRedstoneDriver() {
        setUnlocalizedName(RedstoneBoxesMod.MODID + "." + name);
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(new ResourceLocation(RedstoneBoxesMod.MODID, name));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0,
                                                   new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack stack, EntityPlayer player, List lores, boolean b) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Mode")) {
            lores.add(String.format("Mode: %s", stack.getTagCompound().getString("Mode")));
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
                                      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.FAIL;
        }

        // ensure that there is an NBT tag attached
        NBTTagCompound nbt;
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
            nbt.setString("Mode", KEY.name());
            stack.setTagCompound(nbt);
        }

        // switch modes if sneaking
        // TODO - make this work when not hitting a block, as well
        if (playerIn.isSneaking()) {
            // cycle modes
            if (stack.getTagCompound().getString("Mode").equals(SCAN.name())) {
                nbt.setString("Mode", RECALL.name());
            } else if (stack.getTagCompound().getString("Mode").equals(RECALL.name())) {
                nbt.setString("Mode", KEY.name());
            } else if (stack.getTagCompound().getString("Mode").equals(KEY.name())) {
                nbt.setString("Mode", SCAN.name());
            }

            stack.setTagCompound(nbt);

            return EnumActionResult.FAIL;
        }

        // implement coordinate selection mode
        // redstone mode
        // wrench mode
        // repulsion mode
        // so forth

        if (stack.getTagCompound().getString("Mode").equals(KEY.name())) {
            executeKeyMode(worldIn, playerIn, pos);
        } else if (stack.getTagCompound().getString("Mode").equals(RECALL.name())) {
            executeRecallMode(worldIn, playerIn, pos);
        }

        return EnumActionResult.FAIL;
    }

    protected void executeRecallMode(World worldIn, EntityPlayer playerIn, BlockPos newPos) {
        RedstoneBoxData data = RedstoneBoxData.get(worldIn);
        UUID uuid = playerIn.getUniqueID();

        World extDim = DimensionManager.getWorld(data.getDimension(uuid));
        BlockPos adjPos = newPos.add(0, 2, 0);

        if (!RedstoneBox.canMaterializeAt(extDim, adjPos, data.getRotation(uuid))) {
            // TODO - nudge it around (a max of N attempts)
            playerIn.addChatMessage(new TextComponentString("The Redstone Box cannot materialise there!"));
            return;
        }

        if (playerIn.experienceLevel < 1) {
            playerIn.addChatMessage(new TextComponentString("You need at least one level of experience"));
            return;
        }

        playerIn.addExperienceLevel(-1);
        
        if (data.isMaterialized(uuid)) {
            BlockPos corepos = data.getExteriorPos(uuid);
            RedstoneBox.Dematerialize(extDim, corepos);
        }

        playerIn.addChatMessage(new TextComponentString(String.format("Materialising at %d %d %d",
                                                                      newPos.getX(), newPos.getY(), newPos.getZ())));
        data.setTargetPos(uuid, adjPos);
        RedstoneBox.Materialize(extDim, adjPos, data.getRotation(uuid));
        data.setExteriorPos(uuid, adjPos);
        data.setDimension(uuid, playerIn.dimension);
        data.setMaterialized(uuid, true);
    }

    protected void executeKeyMode(World worldIn, EntityPlayer playerIn, BlockPos pos) {
        int currentDim = playerIn.worldObj.provider.getDimension();

        IBlockState blockstate = worldIn.getBlockState(pos);
        Block block = blockstate.getBlock();
        UUID uuid = playerIn.getUniqueID();

        if (block instanceof RedstoneChassis) {
            BlockPos corepos = ((RedstoneChassis) block).getCore(worldIn, pos);

            if (corepos != null) {
                IBlockState corestate = worldIn.getBlockState(corepos);
                AbstractCore coreblock = (AbstractCore) corestate.getBlock();

                if (coreblock.checkMultiblock(corestate, worldIn, corepos, playerIn)
                    /* && RedstoneBox.canMaterializeAt(worldIn, corepos) */) {
                    // unconditionally create the redstonebox regardless of whether there is space :0)
                    RedstoneBox.createRedstoneBox(worldIn, playerIn, corepos);
                } else {
                    playerIn.addChatMessage(new TextComponentString("The Redstone Box cannot materialize there!"));
                }

                return;
            }
        } else if (block instanceof BTCCube && blockstate.getValue(appearance) > 13) {
            // 14 and 15 are the states for upper and lower door appearances
            if (currentDim == Config.dimensionId) {
                // exit the redstonebox
                RedstoneBoxData data = RedstoneBoxData.get(worldIn);

                if (!data.isMaterialized(uuid)) {
                    playerIn.addChatMessage(new TextComponentString("The Redstone Box is not materialized!"));
                    return;
                }

                // set the position as the current active door
                data.setInteriorPos(uuid, pos);

                // set facing before teleporting, because teleporting will create a new entity
                // also offset the core's position by a reasonable amount
                BlockPos exitPos = data.getExteriorPos(uuid);
                playerIn.rotationPitch = 0;

                int rot = data.getRotation(uuid);

                switch (rot) {
                    case 2:
                        exitPos = exitPos.add(0, -1, -2);
                        playerIn.rotationYaw = 180;
                        break;
                    case 3:
                        exitPos = exitPos.add(0, -1, 2);
                        playerIn.rotationYaw = 0;
                        break;
                    case 4:
                        exitPos = exitPos.add(-2, -1, 0);
                        playerIn.rotationYaw = 90;
                        break;
                    case 5:
                        exitPos = exitPos.add(2, -1, 0);
                        playerIn.rotationYaw = -90;
                        break;
                }

                int dim = data.getDimension(uuid);
                CustomTeleporter.teleportToDimension(playerIn, dim, exitPos.getX(), exitPos.getY(), exitPos.getZ());
            } else {
                // enter the redstonebox
                // TODO - actually check to which redstonebox the block belongs to
                // this just checks if the player clicked inside a 5x4x5 around the clicked block
                RedstoneBoxData data = RedstoneBoxData.get(worldIn);
                BlockPos corepos = data.getExteriorPos(uuid);

                if (Utils.isInAABB(pos, corepos.add(-2, -2, -2), corepos.add(2, 2, 2))) {
                    // get the entry
                    // if the coords are inside the cuboid

                    // TODO - take facing into account (if the door is made relocatable)
                    // set facing
                    playerIn.rotationPitch = 0;
                    playerIn.rotationYaw = 180;

                    BlockPos d = data.getInteriorPos(uuid).add(0, 0, -2);
                    CustomTeleporter.teleportToDimension(playerIn, Config.dimensionId,
                                                         d.getX(), d.getY(), d.getZ());
                }
            }
        }
    }

    public enum Modes {
        SCAN,
        RECALL,
        KEY
    }
}
