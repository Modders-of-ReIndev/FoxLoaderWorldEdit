package com.sk89q.worldedit.foxloader;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.entity.metadata.EntityType;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.NullWorld;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class FoxLoaderEntity implements Entity {
    private final WeakReference<net.minecraft.common.entity.Entity> entityRef;

    FoxLoaderEntity(net.minecraft.common.entity.Entity entity) {
        Preconditions.checkNotNull(entity);
        this.entityRef = new WeakReference<>(entity);
    }

    @Override
    public @Nullable BaseEntity getState() {
        return null;
    }

    @Override
    public Location getLocation() {
        net.minecraft.common.entity.Entity entity = entityRef.get();
        if (entity != null) {
            Vector position = new Vector(entity.posX, entity.posY, entity.posZ);
            float yaw = entity.rotationYaw;
            float pitch = entity.rotationPitch;

            return new Location(new FoxLoaderWorld(entity.worldObj), position, yaw, pitch);
        } else {
            return new Location(NullWorld.getInstance());
        }
    }

    @Override
    public Extent getExtent() {
        net.minecraft.common.entity.Entity entity = entityRef.get();
        if (entity != null) {
            return new FoxLoaderWorld(entity.worldObj);
        } else {
            return NullWorld.getInstance();
        }
    }

    @Override
    public boolean remove() {
        net.minecraft.common.entity.Entity entity = entityRef.get();
        if (entity != null) {
            entity.setEntityDead();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T> T getFacet(Class<? extends T> cls) {
        net.minecraft.common.entity.Entity entity = entityRef.get();
        if (entity != null) {
            if (EntityType.class.isAssignableFrom(cls)) {
                return (T) new FoxLoaderEntityType(entity);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
