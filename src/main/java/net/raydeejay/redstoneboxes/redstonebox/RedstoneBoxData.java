package net.raydeejay.redstoneboxes.redstonebox;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.raydeejay.redstoneboxes.RedstoneBoxesMod;

import java.util.UUID;

public class RedstoneBoxData extends WorldSavedData {
    private static final String PREFIX = RedstoneBoxesMod.MODID;

    private NBTTagCompound data = new NBTTagCompound();

    public RedstoneBoxData() {
        super(PREFIX);
    }

    public RedstoneBoxData(String name) {
        super(name);
    }

    public static RedstoneBoxData get(World world) {
        MapStorage storage = world.getMapStorage();
        RedstoneBoxData instance = (RedstoneBoxData) storage.getOrLoadData(RedstoneBoxData.class, PREFIX);

        if (instance == null) {
            instance = new RedstoneBoxData();
            storage.setData(PREFIX, instance);
        }

        return instance;
    }

    public BlockPos getExteriorPos(UUID uuid) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        return new BlockPos(playerdata.getInteger("extX"),
                            playerdata.getInteger("extY"),
                            playerdata.getInteger("extZ"));
    }

    public NBTTagCompound setExteriorPos(UUID uuid, BlockPos pos) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        playerdata.setInteger("extX", pos.getX());
        playerdata.setInteger("extY", pos.getY());
        playerdata.setInteger("extZ", pos.getZ());
        data.setTag(uuid.toString(), playerdata);
        this.markDirty();
        return data;
    }

    public BlockPos getInteriorPos(UUID uuid) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        return new BlockPos(playerdata.getInteger("intX"),
                            playerdata.getInteger("intY"),
                            playerdata.getInteger("intZ"));
    }

    public NBTTagCompound setInteriorPos(UUID uuid, BlockPos pos) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        playerdata.setInteger("intX", pos.getX());
        playerdata.setInteger("intY", pos.getY());
        playerdata.setInteger("intZ", pos.getZ());
        data.setTag(uuid.toString(), playerdata);
        this.markDirty();
        return data;
    }

    public BlockPos getTargetPos(UUID uuid) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        return new BlockPos(playerdata.getInteger("targetX"),
                            playerdata.getInteger("targetY"),
                            playerdata.getInteger("targetZ"));
    }

    public NBTTagCompound setTargetPos(UUID uuid, BlockPos pos) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        playerdata.setInteger("targetX", pos.getX());
        playerdata.setInteger("targetY", pos.getY());
        playerdata.setInteger("targetZ", pos.getZ());
        data.setTag(uuid.toString(), playerdata);
        this.markDirty();
        return data;
    }

    public BlockPos getCoursePos(UUID uuid) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        return new BlockPos(playerdata.getInteger("courseX"),
                            playerdata.getInteger("courseY"),
                            playerdata.getInteger("courseZ"));
    }

    public NBTTagCompound setCoursePos(UUID uuid, BlockPos pos) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        playerdata.setInteger("courseX", pos.getX());
        playerdata.setInteger("courseY", pos.getY());
        playerdata.setInteger("courseZ", pos.getZ());
        data.setTag(uuid.toString(), playerdata);
        this.markDirty();
        return data;
    }

    public int getRadius(UUID uuid) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        return playerdata.getInteger("radius");
    }

    public NBTTagCompound setRadius(UUID uuid, int value) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        playerdata.setInteger("radius", value);
        data.setTag(uuid.toString(), playerdata);
        this.markDirty();
        return data;
    }

    public int getRotation(UUID uuid) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        return playerdata.getInteger("rotation");
    }

    public NBTTagCompound setRotation(UUID uuid, int value) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        playerdata.setInteger("rotation", value);
        data.setTag(uuid.toString(), playerdata);
        this.markDirty();
        return data;
    }

    public int getDimension(UUID uuid) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        return playerdata.getInteger("dimension");
    }

    public NBTTagCompound setDimension(UUID uuid, int value) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        playerdata.setInteger("dimension", value);
        data.setTag(uuid.toString(), playerdata);
        this.markDirty();
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        data = nbt.getCompoundTag("redstonebox");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("redstonebox", data);
        return compound;
    }

    public boolean isMaterialized(UUID uuid) {
        return data.getCompoundTag(uuid.toString()).getBoolean("materialized");
    }

    public NBTTagCompound setMaterialized(UUID uuid, boolean state) {
        NBTTagCompound playerdata = data.getCompoundTag(uuid.toString());
        playerdata.setBoolean("materialized", state);
        data.setTag(uuid.toString(), playerdata);
        this.markDirty();
        return data;
    }
}
