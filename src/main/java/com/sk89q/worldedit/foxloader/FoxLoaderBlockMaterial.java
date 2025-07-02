package com.sk89q.worldedit.foxloader;

import com.sk89q.worldedit.blocks.BlockMaterial;
import net.minecraft.common.block.Block;
import net.minecraft.common.block.Blocks;
import net.minecraft.common.block.children.*;
import net.minecraft.common.block.data.Materials;

import java.util.Objects;

public class FoxLoaderBlockMaterial implements BlockMaterial {
    private final Block block;

    public FoxLoaderBlockMaterial(Block block) {
        this.block = block;
    }


    @Override
    public boolean isRenderedAsNormalBlock() {
        return this.block.renderAsNormalBlock();
    }

    @Override
    public boolean isFullCube() {
        return this.block.isOpaqueCube() || (this.block.renderAsNormalBlock() &&
                this.block.minX == 0D && this.block.minY == 0D && this.block.minZ == 0D &&
                this.block.maxX == 1D && this.block.maxY == 1D && this.block.maxZ == 1D);
    }

    @Override
    public boolean isOpaque() {
        return this.block.isOpaque();
    }

    @Override
    public boolean isPowerSource() {
        return this.block.canProvidePower();
    }

    @Override
    public boolean isLiquid() {
        return this.block.blockMaterial.getIsLiquid();
    }

    @Override
    public boolean isSolid() {
        return this.block.blockMaterial.getIsSolid();
    }

    @Override
    public float getHardness() {
        return this.block.getHardness();
    }

    @Override
    public float getResistance() {
        return this.block.getResistance();
    }

    @Override
    public float getSlipperiness() {
        return this.block.slipperiness;
    }

    @Override
    public boolean isGrassBlocking() {
        return Blocks.CAN_BLOCK_GRASS.get(this.block.blockID);
    }

    @Override
    public float getAmbientOcclusionLightValue() {
        return 0;
    }

    @Override
    public int getLightOpacity() {
        return this.block.getLightOpacity();
    }

    @Override
    public int getLightValue() {
        return this.block.getLightValueInt();
    }

    @Override
    public boolean isFragileWhenPushed() {
        return false;
    }

    @Override
    public boolean isUnpushable() {
        return this.block.blockMaterial == Materials.OBSIDIAN ||
                this.block instanceof BlockContainer ||
                this.block.getMobilityFlag() == 2 ||
                this.block.getHardness() == -1;
    }

    @Override
    public boolean isAdventureModeExempt() {
        return false;
    }

    @Override
    public boolean isTicksRandomly() {
        return Blocks.TICK_ON_LOAD.get(this.block.blockID);
    }

    @Override
    public boolean isUsingNeighborLight() {
        return false;
    }

    @Override
    public boolean isMovementBlocker() {
        return this.block.isCollidable();
    }

    @Override
    public boolean isBurnable() {
        return Blocks.CHANCE_TO_ENCOURAGE_FIRE[this.block.blockID] > 0;
    }

    @Override
    public boolean isToolRequired() {
        return this.block.getMinHarvestLevel() > 0;
    }

    @Override
    public boolean isReplacedDuringPlacement() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FoxLoaderBlockMaterial that)) return false;
        return Objects.equals(this.block, that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.block);
    }
}
