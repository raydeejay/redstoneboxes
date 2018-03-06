package net.raydeejay.redstoneboxes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class Utils {
    public static void loadStructure(BlockPos pos, World world, String name, Rotation rotation) {
        WorldServer worldserver = (WorldServer) world;
        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        ResourceLocation loc = new ResourceLocation(RedstoneBoxesMod.MODID, name);
        Template template = templatemanager.get(minecraftserver, loc);

        if (template != null) {
            IBlockState iblockstate = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
            PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE)
                    .setRotation(rotation).setIgnoreEntities(false).setChunk((ChunkPos) null)
                    .setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

            template.addBlocksToWorldChunk(world, pos.add(0, 1, 0), placementsettings);
        }
    }

    public static boolean isInAABB(BlockPos pos, BlockPos min, BlockPos max) {
        return ((pos.getX() <= max.getX()) && (pos.getX() >= min.getX())
                && (pos.getZ() <= max.getZ()) && (pos.getZ() >= min.getZ())
                && (pos.getY() <= max.getY()) && (pos.getY() >= min.getY()));
    }
}
