package com.sk89q.worldedit.foxloader;

import com.sk89q.worldedit.world.registry.BlockRegistry;
import com.sk89q.worldedit.world.registry.LegacyWorldData;

public class FoxLoaderWorldData extends LegacyWorldData {
    private static final FoxLoaderWorldData INSTANCE = new FoxLoaderWorldData();

    private final FoxLoaderBlockRegistry blockRegistry = new FoxLoaderBlockRegistry();
    private final FoxLoaderItemRegistry itemRegistry = new FoxLoaderItemRegistry();

    @Override
    public BlockRegistry getBlockRegistry() {
        return blockRegistry;
    }

    @Override
    public FoxLoaderItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public static FoxLoaderWorldData getInstance() {
        return INSTANCE;
    }
}
