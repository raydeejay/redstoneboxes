package net.raydeejay.redstoneboxes.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class RedboxTerrainGenerator {
    private World world;

    public RedboxTerrainGenerator() {
    }

    public void setBiomesForGeneration(Biome[] biomesForGeneration) {
    }

    public void setup(World world, Random rand) {
        this.world = world;
    }

    public void generate(int chunkX, int chunkZ, ChunkPrimer primer) {
        // just load a structure at the appropriate points in the chunk generator
//        if (chunkX == 0 && chunkZ == 0) {
//            // spawn the 16x16x16 cube, 15x15x15 plus padding in 2 of the walls
//            // interior is 14 high, lower level is 6 high, upper level 5 high
//            // 2 floors 1 roof
//            // levers in the top and bottom layers
//            for (int y = 96; y < 110; ++y) {
//                for (int z = 0; z < 15; ++z) {
//                    primer.setBlockState(0, y, z, Blocks.STONE.getStateFromMeta(6));
//                }
//                for (int x = 1; x < 14; ++x) {
//                    primer.setBlockState(x, y, 0, Blocks.STONE.getStateFromMeta(6));
//                    primer.setBlockState(x, y, 14, Blocks.STONE.getStateFromMeta(6));
//                }
//                for (int z = 0; z < 15; ++z) {
//                    primer.setBlockState(14, y, z, Blocks.STONE.getStateFromMeta(6));
//                }
//            }
//
//            // floor and roof
//            for (int z = 1; z < 14; ++z) {
//                for (int x = 1; x < 14; ++x) {
//                    int y = 96;
//                    primer.setBlockState(x, y, z, Blocks.QUARTZ_BLOCK.getDefaultState());
//                }
//            }
//
//            for (int z = 1; z < 14; ++z) {
//                for (int x = 1; x < 14; ++x) {
//                    int y = 109;
//                    primer.setBlockState(x, y, z, Blocks.QUARTZ_BLOCK.getDefaultState());
//                }
//            }
//
//            // upper level floor
//            for (int z = 1; z < 4; ++z) {
//                int y = 103;
//                for (int x = 1; x < 14; ++x) {
//                    primer.setBlockState(x, y, z, Blocks.QUARTZ_BLOCK.getDefaultState());
//                    primer.setBlockState(x, y, 14 - z, Blocks.QUARTZ_BLOCK.getDefaultState());
//                }
//            }
//
//            for (int z = 1; z < 14; ++z) {
//                int y = 103;
//                for (int x = 1; x < 4; ++x) {
//                    primer.setBlockState(x, y, z, Blocks.QUARTZ_BLOCK.getDefaultState());
//                    primer.setBlockState(14 - x, y, z, Blocks.QUARTZ_BLOCK.getDefaultState());
//                }
//            }
//
//            // wall passage markers
//            primer.setBlockState(6, 97, 14, Blocks.STONEBRICK.getStateFromMeta(3));
//            primer.setBlockState(6, 98, 14, Blocks.STONEBRICK.getStateFromMeta(3));
//            primer.setBlockState(6, 99, 14, Blocks.STONEBRICK.getStateFromMeta(3));
//
//            primer.setBlockState(7, 99, 14, Blocks.STONEBRICK.getStateFromMeta(3));
//
//            primer.setBlockState(8, 97, 14, Blocks.STONEBRICK.getStateFromMeta(3));
//            primer.setBlockState(8, 98, 14, Blocks.STONEBRICK.getStateFromMeta(3));
//            primer.setBlockState(8, 99, 14, Blocks.STONEBRICK.getStateFromMeta(3));
//
//            // real world interface
//
//            primer.setBlockState(7, 97, 14, ModBlocks.btcCube.getStateFromMeta(15));
//            primer.setBlockState(7, 98, 14, ModBlocks.btcCube.getStateFromMeta(14));
//
//
//        }
    }
}

