package com.sk89q.worldedit.foxloader;

import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.world.registry.ItemRegistry;
import net.minecraft.common.item.Item;
import net.minecraft.common.item.Items;
import org.jetbrains.annotations.Nullable;

public class FoxLoaderItemRegistry implements ItemRegistry {
    @Override
    public @Nullable BaseItem createFromId(String id) {
        Item item = Item.getItemByName(id);
        return item == null ? null :
                new BaseItem(item.itemID);
    }

    @Override
    public @Nullable BaseItem createFromId(int id) {
        Item item = Items.ITEMS_LIST[id];
        return item.itemID != id ? null :
                new BaseItem(item.itemID);
    }
}
