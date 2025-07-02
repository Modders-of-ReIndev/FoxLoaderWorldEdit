package com.sk89q.worldedit.foxloader;

import com.google.common.base.Preconditions;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.IntTag;
import com.sk89q.worldedit.Vector;

import java.lang.reflect.Constructor;
import javax.annotation.Nullable;
import net.minecraft.common.block.tileentity.TileEntity;
import net.minecraft.common.world.World;

public class TileEntityUtils {

    private TileEntityUtils() {
    }

    /**
     * Update the given tag compound with position information.
     *
     * @param tag the tag
     * @param position the position
     * @return a tag compound
     */
    private static CompoundTag updateForSet(CompoundTag tag, Vector position) {
        Preconditions.checkNotNull(tag);
        Preconditions.checkNotNull(position);

        tag.setTag("x", new IntTag(position.getBlockX()));
        tag.setTag("y", new IntTag(position.getBlockY()));
        tag.setTag("z", new IntTag(position.getBlockZ()));

        return tag;
    }

    /**
     * Set a tile entity at the given location.
     *
     * @param world the world
     * @param position the position
     * @param clazz the tile entity class
     * @param tag the tag for the tile entity (may be null to not set NBT data)
     */
    static void setTileEntity(World world, Vector position, Class<? extends TileEntity> clazz, @Nullable CompoundTag tag) {
        Preconditions.checkNotNull(world);
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(clazz);

        TileEntity tileEntity = constructTileEntity(world, position, clazz);

        if (tileEntity == null) {
            return;
        }

        if (tag != null) {
            // Set X, Y, Z
            updateForSet(tag, position);
            tileEntity.readFromNBT(tag);
        }

        world.setBlockTileEntity(position.getBlockX(), position.getBlockY(), position.getBlockZ(), tileEntity);
    }

    /**
     * Set a tile entity at the given location using the tile entity ID from
     * the tag.
     *
     * @param world the world
     * @param position the position
     * @param tag the tag for the tile entity (may be null to do nothing)
     */
    static void setTileEntity(World world, Vector position, @Nullable CompoundTag tag) {
        if (tag != null) {
            updateForSet(tag, position);
            TileEntity tileEntity = TileEntity.createAndLoadEntity(world, tag);
            if (tileEntity != null) {
                world.setBlockTileEntity(position.getBlockX(), position.getBlockY(), position.getBlockZ(), tileEntity);
            }
        }
    }

    /**
     * Construct a tile entity from the given class.
     *
     * @param world the world
     * @param position the position
     * @param clazz the class
     * @return a tile entity (may be null if it failed)
     */
    @Nullable
    static TileEntity constructTileEntity(World world, Vector position, Class<? extends TileEntity> clazz) {
        Constructor<? extends TileEntity> baseConstructor;
        try {
            baseConstructor = clazz.getConstructor(); // creates "blank" TE
        } catch (Throwable e) {
            return null; // every TE *should* have this constructor, so this isn't necessary
        }

        TileEntity genericTE;
        try {
            genericTE = baseConstructor.newInstance();
        } catch (Throwable e) {
            return null;
        }

        return genericTE;
    }

}
