package com.sk89q.worldedit.foxloader;

import com.fox2code.foxloader.registry.missing.MissingBlock;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockMaterial;
import com.sk89q.worldedit.world.registry.BlockRegistry;
import com.sk89q.worldedit.world.registry.State;
import net.minecraft.common.block.Block;
import net.minecraft.common.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class FoxLoaderBlockRegistry implements BlockRegistry {

    @Override
    public @Nullable BaseBlock createFromId(String id) {
        String blockIdStr = Block.getBlockByName(id);
        int blockId;
        try {
            blockId = Integer.parseInt(blockIdStr);
        } catch (RuntimeException ignored) {
            return null;
        }
        return createFromId(blockId);
    }

    @Override
    public @Nullable BaseBlock createFromId(int id) {
        Block block = Blocks.BLOCKS_LIST[id];
        if (block.blockID != id) {
            return null;
        }
        return new BaseBlock(id);
    }

    @Override
    public @Nullable BlockMaterial getMaterial(BaseBlock block) {
        Block nativeBlock = Blocks.BLOCKS_LIST[block.getId()];
        if (nativeBlock instanceof MissingBlock) {
            return null;
        }
        return new FoxLoaderBlockMaterial(nativeBlock);
    }

    @Override
    public @Nullable Map<String, ? extends State> getStates(BaseBlock block) {
        return Collections.emptyMap();
    }
}
