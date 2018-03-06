package net.raydeejay.redstoneboxes.redstonebox;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import net.raydeejay.redstoneboxes.block.exterior.AbstractCore;
import net.raydeejay.redstoneboxes.block.exterior.RedstoneChassis;
import org.apache.logging.log4j.Level;

import java.util.UUID;

import static net.raydeejay.redstoneboxes.Utils.loadStructure;

public class RedstoneBox {
    public RedstoneBox() {
    }

    public static void createRedstoneBox(World world, EntityPlayer playerIn, BlockPos corepos) {
        if (world.isRemote) {
            return;
        }

        // TODO - load the next created redstonebox id
        // TODO - calculate the proper coordinates
        int x = 0;
        int y = 95;
        int z = 0;

        UUID uuid = playerIn.getUniqueID();
        RedstoneBoxData data = RedstoneBoxData.get(world);

        // create the TARDIS in the redstonebox dimension
//        World tardisDim = getWorld(Config.dimensionId);
//        for (int i = -1; i < 2; ++i)
//            for (int j = -1; j < 2; ++j)
//                world.getMinecraftServer().worldServerForDimension(Config.dimensionId).getChunkProvider()
// .provideChunk(i, j);

        BlockPos roomPos = new BlockPos(x, y, z);
        data.setInteriorPos(uuid, roomPos.add(7, 2, 14));
//        loadStructure(roomPos, tardisDim, "redstone_box_control_room", Rotation.NONE);

        // remove the 3x3x3
        deleteChassis(world, corepos);

        // materialize the redstonebox in its place
        data.setExteriorPos(uuid, corepos);
        data.setDimension(uuid, playerIn.dimension);

        // TODO - make the door face the player on creation
        // TODO - ensure that there's enough space
        Materialize(world, corepos, data.getRotation(uuid));
    }

    public static void deleteChassis(World world, BlockPos pos) {
        int x0 = -2;
        int x1 = 2;
        int y0 = -1;
        int y1 = 2;
        int z0 = -2;
        int z1 = 2;

        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    BlockPos testedPos = pos.add(x, y, z);
                    if (world.getBlockState(testedPos).getBlock() instanceof RedstoneChassis ||
                        world.getBlockState(testedPos).getBlock() instanceof AbstractCore) {
                        world.setBlockToAir(testedPos);
                        world.notifyBlockUpdate(testedPos,
                                                Blocks.AIR.getDefaultState(), // why not...
                                                Blocks.AIR.getDefaultState(),
                                                0); // ???
                    }
                }
            }
        }
    }

    public static void Materialize(World world, BlockPos corepos, int rotationIndex) {
        // unconditionally materialize
        // user method has to check first
        // materialize in the proper place
        // position is relative to the place that the core occupied
        BlockPos loadPos; // TODO - figure out why -2 instead of -1

        // take orientation into account
        int x0, x1, y0, y1, z0, z1;
        x0 = -2;
        x1 = 2;
        y0 = -1;
        y1 = 2;
        z0 = -2;
        z1 = 2;

        // translate the core position to load position according to the facing
        // note that rotation and facing are not the same thing, hence the difference in order
        RedstoneBoxesMod.logger.log(Level.INFO, String.format("the number is %d", rotationIndex));


        switch (rotationIndex) {
            case 2:
                // north
                loadPos = corepos.add(-1, -2, -2);
                break;
            case 3:
                // south
                loadPos = corepos.add(1, -2, 2);
                break;
            case 4:
                // west
                loadPos = corepos.add(-2, -2, 1);
                break;
            case 5:
                // east
                loadPos = corepos.add(2, -2, -1);
                break;
            default:
                // so the compiler doesn't bark, default to north?
                loadPos = corepos.add(-1, -2, -2);
                rotationIndex = 2;
                break;
        }

        Rotation rots[] = {Rotation.NONE,
                Rotation.CLOCKWISE_180,
                Rotation.COUNTERCLOCKWISE_90,
                Rotation.CLOCKWISE_90
        };

        // possibly unneeded ternary check
        loadStructure(loadPos, world, "redstone_box_exterior", rots[rotationIndex - 2]);

        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    BlockPos p = corepos.add(x, y, z);
                    // TODO - this is not exactly working always...
                    world.notifyBlockUpdate(p, Blocks.AIR.getDefaultState(), world.getBlockState(p), 0);
                }
            }
        }

        // TODO - extend platform if needed
    }

    public static void deleteRedstoneBox() {
        // delete the relevant variables
        // optionally delete the blocks
    }

    public static void Dematerialize(World world, BlockPos corepos) {
        // remove platform if exists
        // dematerialize
        // TODO - fix, removes blocks in a 5x4x5
        int x0 = -2;
        int x1 = 2;
        int y0 = -1;
        int y1 = 2;
        int z0 = -2;
        int z1 = 2;

        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    BlockPos p = corepos.add(x, y, z);
                    // TODO - figure out why this barks
                    try {
                        IBlockState previousState = world.getBlockState(p);
                        world.setBlockToAir(p);
                        world.notifyBlockUpdate(p, previousState, Blocks.AIR.getDefaultState(), 0);
                    } catch (Exception e) {
                        // warn
                        RedstoneBoxesMod.logger.log(Level.WARN, "exception removing a redstonebox block");
                    }
                }
            }
        }
    }

    public static boolean canMaterializeAt(World world, BlockPos pos, int rotationIndex) {
        int airBlocks = 0;

        // TODO - fix, checking 5x4x5 around the core to ensure free space right now
        int x0 = -2;
        int x1 = 2;
        int y0 = -1;
        int y1 = 2;
        int z0 = -2;
        int z1 = 2;

        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    Block block = world.getBlockState(pos.add(x, y, z)).getBlock();
                    if (block instanceof BlockAir
                        || block instanceof BlockLeaves
                        || block instanceof BlockTallGrass
                        || block instanceof BlockFlower
                        || block instanceof BlockTorch) {
                        ++airBlocks;
                    }
                }
            }
        }

        return airBlocks == 5 * 4 * 5;
    }
}
