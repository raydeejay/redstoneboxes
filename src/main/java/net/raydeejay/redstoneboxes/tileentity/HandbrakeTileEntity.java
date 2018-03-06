package net.raydeejay.redstoneboxes.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HandbrakeTileEntity extends TileEntity implements ITickable {
    public static int travelTime = 10 * 20;
    public int timeRemaining = -1;

    // TODO - this may not be a good idea
    public HandbrakeTileEntity() {
        super();
    }

    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    public void startTravel() {
        timeRemaining = travelTime;
        // play sound
    }

    public boolean isTraveling() {
        return timeRemaining > -1;
    }

    public int getField(int id) {
        switch (id) {
            case 0:
                return this.timeRemaining;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.timeRemaining = value;
                break;
        }
    }

    @Override
    public void update() {
        // here we count down since the handbrake was releasaed
        // play sounds, consume fuel, until the trip is over

        if (timeRemaining > -1) {
            // play sounds, update things, animation
            // chances of events during flight happen here

            if (timeRemaining == 0) {
                // message that we arrived
                BlockPos pos = this.getPos();
                this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                                          SoundEvents.BLOCK_PORTAL_TRIGGER,
                                          SoundCategory.PLAYERS,
                                          0.666F, 0.666F);
            }

            --timeRemaining;
        }
    }
}
