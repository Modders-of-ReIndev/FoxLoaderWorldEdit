package com.sk89q.worldedit.foxloader;

import com.google.common.base.Preconditions;
import com.mojang.nbt.CompoundTag;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.*;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.AbstractWorld;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.registry.WorldData;
import net.minecraft.common.block.Block;
import net.minecraft.common.block.Blocks;
import net.minecraft.common.block.children.BlockContainer;
import net.minecraft.common.block.fluid.Fluid;
import net.minecraft.common.block.fluid.Fluids;
import net.minecraft.common.block.tileentity.TileEntity;
import net.minecraft.common.entity.inventory.IInventory;
import net.minecraft.common.entity.other.EntityItem;
import net.minecraft.common.world.World;
import net.minecraft.common.world.WorldGenerator;
import net.minecraft.common.world.features.WorldGenBigTree;
import net.minecraft.common.world.features.WorldGenSpruce;
import net.minecraft.common.world.features.WorldGenTrees;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FoxLoaderWorld extends AbstractWorld {
    private static final Random random = new Random();

    private final WeakReference<World> worldRef;
    private final int initialLowestY, initialHighestY;

    FoxLoaderWorld(World world) {
        Preconditions.checkNotNull(world);
        this.worldRef = new WeakReference<>(world);
        this.initialLowestY = Math.min(world.lowestY, 0);
        this.initialHighestY = Math.max(world.highestY, 255);
    }

    public World getWorldChecked() throws WorldEditException {
        World world = worldRef.get();
        if (world != null) {
            return world;
        } else {
            throw new WorldReferenceLostException("The reference to the world was lost (i.e. the world may have been unloaded)");
        }
    }

    public World getWorld() {
        World world = worldRef.get();
        if (world != null) {
            return world;
        } else {
            throw new RuntimeException("The reference to the world was lost (i.e. the world may have been unloaded)");
        }
    }

    @Override
    public String getName() {
        return getWorldName(this.getWorld());
    }

    static String getWorldName(World world) {
        return world.getWorldInfo().getWorldName();
    }

    @Override
    public boolean setBlock(Vector position, BaseBlock block, boolean notifyAndLight) throws WorldEditException {
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(block);

        World world = getWorldChecked();
        int x = position.getBlockX();
        int y = position.getBlockY();
        int z = position.getBlockZ();

        // Note: There is no API to disable light updates in ReIndev
        int oldId = world.getBlockId(x, y, z);
        boolean successful = world.setBlockNoUpdate(x, y, z, block.getId());
        if (successful || oldId == block.getId()) {
            successful |= world.setBlockMetadata(x, y, z, block.getData());
        }
        if (successful && block.hasNbtData()) {
            world.removeBlockTileEntity(x, y, z);
            CompoundTag nativeTag = NBTConverter.toNative(Objects.requireNonNull(block.getNbtData()));
            nativeTag.setString("id", block.getNbtId());
            TileEntityUtils.setTileEntity(world, position, nativeTag);
        }
        if (notifyAndLight) {
            world.notifyBlockChange(x, y, z, successful ? block.getId() : oldId);
        }

        return successful;
    }

    @Override
    public int getBlockLightLevel(Vector position) {
        Preconditions.checkNotNull(position);
        return this.getWorld().getBlockLightValue(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    @Override
    public boolean clearContainerBlockContents(Vector position) {
        Preconditions.checkNotNull(position);
        TileEntity tileEntity = this.getWorld()
                .getBlockTileEntityImmediate(position.getBlockX(), position.getBlockY(), position.getBlockZ());
        if ((tileEntity instanceof IInventory inv)) {
            int size = inv.getSizeInventory();
            for (int i = 0; i < size; i++) {
                inv.setInventorySlotContents(i, null);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean regenerate(Region region, EditSession editSession) {
        return false;
    }

    @Nullable
    private static WorldGenerator createWorldGenerator(TreeGenerator.TreeType type) {
        switch (type) {
            case TREE: return new WorldGenTrees();
            case BIG_TREE: return new WorldGenBigTree();
            case REDWOOD: return new WorldGenSpruce();
            default: return null;
        }
    }

    @Override
    public boolean generateTree(TreeGenerator.TreeType type, EditSession editSession, Vector position) throws MaxChangedBlocksException {
        WorldGenerator generator = createWorldGenerator(type);
        return generator != null ? generator.generateUnsafe(this.getWorld(), random,
                position.getBlockX(), position.getBlockY(), position.getBlockZ()) : false;
    }

    @Override
    public WorldData getWorldData() {
        return FoxLoaderWorldData.getInstance();
    }

    @Override
    public boolean isValidBlockType(int id) {
        Block block = Blocks.BLOCKS_LIST[id];
        return block.blockID == id;
    }

    @Override
    public boolean usesBlockData(int type) {
        Block block = Blocks.BLOCKS_LIST[type];
        return block.blockID == type &&
                block instanceof BlockContainer;
    }

    @Override
    public Mask createLiquidMask() {
        // TODO: Use FoxLoader API once it exists.
        Fluid[] fluids = new Fluid[]{
                Fluids.WATER, Fluids.LAVA, Fluids.ACID, Fluids.SANGUIS
        };
        ArrayList<BaseBlock> wFluids = new ArrayList<>();
        for (Fluid fluid : fluids) {
            wFluids.add(new BaseBlock(fluid.getMoving().blockID, -1));
            wFluids.add(new BaseBlock(fluid.getStill().blockID, -1));
        }
        return new BlockMask(this, wFluids);
    }

    @Override
    public Vector getMinimumPoint() {
        int minY = this.initialLowestY;
        World world = this.worldRef.get();
        if (world != null) {
            minY = world.lowestY;
        }
        return new Vector(-32000000, minY, -32000000);
    }

    @Override
    public Vector getMaximumPoint() {
        int maxY = this.initialHighestY;
        World world = this.worldRef.get();
        if (world != null) {
            maxY = world.highestY;
        }
        return new Vector(32000000, maxY, 32000000);
    }

    @Override
    public List<? extends Entity> getEntities(Region region) {
        return List.of();
    }

    @Override
    public List<? extends Entity> getEntities() {
        return List.of();
    }

    @Override
    public @Nullable Entity createEntity(Location location, BaseEntity entity) {
        return null;
    }

    @Override
    public BaseBlock getBlock(Vector position) {
        World world = this.getWorld();
        int x = position.getBlockX();
        int y = position.getBlockY();
        int z = position.getBlockZ();
        int blockId = world.getBlockId(x, y, z);
        int blockData = world.getBlockMetadata(x, y, z);
        TileEntity tileEntity = world.getBlockTileEntityImmediate(x, y, z);
        if (tileEntity != null) {
            return new TileEntityBaseBlock(blockId, blockData, tileEntity);
        } else {
            return new BaseBlock(blockId, blockData);
        }
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        World world = this.getWorld();
        int x = position.getBlockX();
        int y = position.getBlockY();
        int z = position.getBlockZ();
        int blockId = world.getBlockId(x, y, z);
        int blockData = world.getBlockMetadata(x, y, z);
        return new LazyBlock(blockId, blockData, this, position);
    }

    @Override
    public int hashCode() {
        return this.getWorld().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FoxLoaderWorld that)) return false;
        return Objects.equals(this.getWorld(), that.getWorld());
    }

    @Override
    public BaseBiome getBiome(Vector2D position) {
        return new BaseBiome(0);
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        return false;
    }

    @Override
    public boolean useItem(Vector position, BaseItem item, Direction face) {
        return false;
    }

    @Override
    public void dropItem(Vector position, BaseItemStack item) {
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(item);

        if (item.getType() == 0) {
            return;
        }

        World world = this.getWorld();
        EntityItem entityItem = new EntityItem(world,
                position.getX(), position.getY(), position.getZ(),
                FoxLoaderWorldEdit.toFoxLoaderItemStack(item));
        world.entityJoinedWorld(entityItem);
    }

    private static class WorldReferenceLostException extends WorldEditException {
        private WorldReferenceLostException(String message) {
            super(message);
        }
    }
}
