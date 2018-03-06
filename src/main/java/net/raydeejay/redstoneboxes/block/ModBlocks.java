package net.raydeejay.redstoneboxes.block;

import net.minecraft.block.Block;
import net.raydeejay.redstoneboxes.block.controls.*;
import net.raydeejay.redstoneboxes.block.exterior.AbstractCore;
import net.raydeejay.redstoneboxes.block.exterior.BTCCube;
import net.raydeejay.redstoneboxes.block.exterior.RedstoneChassis;

public class ModBlocks {
    public static AbstractCore abstractCore;
    public static RedstoneChassis redstoneChassis;
    public static Randomizer randomizer;
    public static Handbrake handbrake;
    public static RadiusSelector radiusSelector;
    public static CourseSetter courseSetter;
    public static CoordinatesSelector coordinatesSelector;
    public static BTCCube btcCube;
    public static DimensionSelector dimensionsSelector;
    public static RotationSelector rotationSelector;

    public static final Block[] BLOCKS = new Block[]{
            abstractCore =  new AbstractCore(),
            redstoneChassis = new RedstoneChassis(),
            randomizer = new Randomizer(),
            handbrake = new Handbrake(),
            radiusSelector = new RadiusSelector(),
            coordinatesSelector = new CoordinatesSelector(),
            courseSetter = new CourseSetter(),
            btcCube = new BTCCube(),
            dimensionsSelector = new DimensionSelector(),
            rotationSelector = new RotationSelector()
    };
}
