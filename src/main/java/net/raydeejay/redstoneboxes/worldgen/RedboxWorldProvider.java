package net.raydeejay.redstoneboxes.worldgen;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

public class RedboxWorldProvider extends WorldProvider {
    @Override
    public DimensionType getDimensionType() {
        return ModDimensions.redboxDimensionType;
    }

    @Override
    public String getSaveFolder() {
        return "REDBOX";
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new RedboxChunkGenerator(worldObj);
    }
}
