package com.sk89q.worldedit.foxloader;

import com.mojang.nbt.CompoundTag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.TileEntityBlock;
import net.minecraft.common.block.tileentity.TileEntity;

public class TileEntityBaseBlock extends BaseBlock implements TileEntityBlock {
    public TileEntityBaseBlock(int id, int data, TileEntity tile) {
        super(id, data);
        CompoundTag compoundTag = new CompoundTag();
        tile.writeToNBT(compoundTag);
        setNbtData(NBTConverter.fromNative(compoundTag));
    }
}
