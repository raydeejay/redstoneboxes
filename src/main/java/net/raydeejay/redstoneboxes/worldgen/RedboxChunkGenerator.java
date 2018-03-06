package net.raydeejay.redstoneboxes.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static net.raydeejay.redstoneboxes.Utils.loadStructure;

public class RedboxChunkGenerator implements IChunkGenerator {
    private final World worldObj;
    private Random random;
    private Biome[] biomesForGeneration;

    //    private MapGenBase caveGenerator = new MapGenCaves();
    private RedboxTerrainGenerator terraingen = new RedboxTerrainGenerator();

    public RedboxChunkGenerator(World worldObj) {
        this.worldObj = worldObj;
        long seed = worldObj.getSeed();
        this.random = new Random((seed + 516) * 314);
        terraingen.setup(worldObj, random);
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        ChunkPrimer chunkprimer = new ChunkPrimer();

        // Setup biomes for terraingen
//        this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration,
//                                                                                           x * 4 - 2, z * 4 - 2, 10,
//                                                                                           10);
//        terraingen.setBiomesForGeneration(biomesForGeneration);
        terraingen.generate(x, z, chunkprimer);

//        // Setup biomes again for actual biome decoration
//        this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z *
// 16, 16, 16);
//        // This will replace stone with the biome specific stones
//        terraingen.replaceBiomeBlocks(x, z, chunkprimer, this, biomesForGeneration);

        // Generate caves
        //this.caveGenerator.generate(this.worldObj, x, z, chunkprimer);

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);

//        byte[] biomeArray = chunk.getBiomeArray();
//        for (int i = 0; i < biomeArray.length; ++i) {
//            biomeArray[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[i]);
//        }
//
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {
//        int i = x * 16;
//        int j = z * 16;
//        BlockPos blockpos = new BlockPos(i, 0, j);
//        Biome biome = this.worldObj.getBiome(blockpos.add(16, 0, 16));
//
//        // Add biome decorations (like flowers, grass, trees, ...)
//        biome.decorate(this.worldObj, this.random, blockpos);
//
//        // Make sure animals appropriate to the biome spawn here when the chunk is generated
//        WorldEntitySpawner.performWorldGenSpawning(this.worldObj, biome, i + 8, j + 8, 16, 16, this.random);

        // TODO - load in the proper places or fix the actual structure data
        // TODO - improve arrangement?

        // TARDIS are reserved 64x64 chunks, the initial room is placed at relative 0,0
        // player feet stand on a block at height 97

        // save the next_tardis_id to the player's redstonebox data
        // increment the next redstonebox id
        // this gives room for about 30k redstonebox in a single dimension...

        // place a new redstonebox at chunk next_tardis_id (starts at 0) * 64 along the X axis, for example
        if (x % 64 == 0 && z == 0) {
            RedstoneBoxesMod.logger.log(Level.INFO, String.format("X %d Z %d", x, z));
            loadStructure(new BlockPos(x * 16, 95, z * 16), worldObj, "redstone_box_control_room", Rotation.NONE);
        }
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        // If you want normal creatures appropriate for this biome then uncomment the
        // following two lines:

        // Biome biome = this.worldObj.getBiome(pos);
        // return biome.getSpawnableList(creatureType);

//        if (creatureType == EnumCreatureType.MONSTER) {
//            return mobs;
//        }

        return ImmutableList.of();
    }

    @Nullable
    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }
}
