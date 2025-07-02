package com.sk89q.worldedit.foxloader;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.entity.metadata.EntityType;
import net.minecraft.common.entity.Entity;
import net.minecraft.common.entity.EntityLiving;
import net.minecraft.common.entity.animals.EntityAnimal;
import net.minecraft.common.entity.animals.EntityFox;
import net.minecraft.common.entity.animals.EntityWolf;
import net.minecraft.common.entity.other.*;
import net.minecraft.common.entity.player.EntityPlayer;
import net.minecraft.common.entity.projectile.EntityThrownArrow;
import net.minecraft.common.entity.projectile.EntityThrownBrick;
import net.minecraft.common.entity.projectile.EntityThrownEgg;

public class FoxLoaderEntityType implements EntityType {
    private final Entity entity;

    public FoxLoaderEntityType(Entity entity) {
        Preconditions.checkNotNull(entity);
        this.entity = entity;
    }

    @Override
    public boolean isPlayerDerived() {
        return this.entity instanceof EntityPlayer;
    }

    @Override
    public boolean isProjectile() {
        return this.entity != null && this.entity.getClass().getName()
                .startsWith("net.minecraft.common.entity.projectile.");
    }

    @Override
    public boolean isItem() {
        return this.entity instanceof EntityItem;
    }

    @Override
    public boolean isFallingBlock() {
        return this.entity instanceof EntityFallingSand;
    }

    @Override
    public boolean isPainting() {
        return this.entity instanceof EntityHangingPainting;
    }

    @Override
    public boolean isItemFrame() {
        return this.entity instanceof EntityHangingItemFrame;
    }

    @Override
    public boolean isBoat() {
        return this.entity instanceof EntityBoat;
    }

    @Override
    public boolean isMinecart() {
        return this.entity instanceof EntityMinecart;
    }

    @Override
    public boolean isTNT() {
        return this.entity instanceof EntityTNT;
    }

    @Override
    public boolean isExperienceOrb() {
        return false;
    }

    @Override
    public boolean isLiving() {
        return this.entity instanceof EntityLiving;
    }

    @Override
    public boolean isAnimal() {
        return this.entity instanceof EntityAnimal;
    }

    @Override
    public boolean isAmbient() {
        return false;
    }

    @Override
    public boolean isNPC() {
        return false;
    }

    @Override
    public boolean isGolem() {
        return false;
    }

    @Override
    public boolean isTamed() {
        if (this.entity instanceof EntityFox entityFox) {
            return entityFox.isTamed();
        } else if (this.entity instanceof EntityWolf entityWolf) {
            return entityWolf.isTamed();
        } else {
            return false;
        }
    }

    @Override
    public boolean isTagged() {
        return this.entity instanceof EntityLiving entityLiving && entityLiving.hasNameTag();
    }

    @Override
    public boolean isArmorStand() {
        return false;
    }

    @Override
    public boolean isPasteable() {
        // We do not support pasting entities yet
        return false;
    }
}
