package com.sk89q.worldedit.foxloader;

import com.fox2code.foxevents.EventHandler;
import com.fox2code.foxloader.event.GlobalTickEvent;
import com.fox2code.foxloader.event.interaction.PlayerStartBreakBlockEvent;
import com.fox2code.foxloader.event.interaction.PlayerUseItemOnAirEvent;
import com.fox2code.foxloader.event.interaction.PlayerUseItemOnBlockEvent;
import com.fox2code.foxloader.event.lifecycle.LifecycleStartEvent;
import com.fox2code.foxloader.event.lifecycle.LifecycleStopEvent;
import com.fox2code.foxloader.launcher.FoxLauncher;
import com.fox2code.foxloader.loader.Mod;
import com.fox2code.foxloader.loader.ModLoader;
import com.fox2code.foxloader.selection.PlayerSelectionProvider;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.event.platform.PlatformReadyEvent;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import net.minecraft.common.item.ItemStack;
import net.minecraft.common.util.FileUtils;

import java.io.File;

public class FoxLoaderWorldEdit extends Mod {
    private FoxLoaderPlatform platform;
    private FoxLoaderConfiguration config;
    private File workingDir;
    private boolean notifyReady;

    public static FoxLoaderWorldEdit inst;

    public FoxLoaderWorldEdit() {
        inst = this;
    }

    @Override
    public void onPreInit() {
        this.workingDir = new File(ModLoader.getConfigFolder(), "worldedit");
        FileUtils.createFolder(this.workingDir);

        config = new FoxLoaderConfiguration(this);
        config.load();

        PlayerSelectionProvider.setImplementation(PlayerSelectionProviderWorldEdit.INSTANCE);
        CommandWrapper.register();

        if (FoxLauncher.isClient()) {
            this.getLogger().warning("FoxLoader WorldEdit is currently experimental client side.");
        }
    }

    FoxLoaderConfiguration getConfig() {
        return this.config;
    }

    public FoxLoaderPlatform getPlatform() {
        return this.platform;
    }

    public File getWorkingDir() {
        return this.workingDir;
    }

    @EventHandler
    public void onServerStart(LifecycleStartEvent lifecycleStartEvent) {
        if (!lifecycleStartEvent.getConnectionType().isServer) return;

        this.platform = new FoxLoaderPlatform(this);
        WorldEdit.getInstance().getPlatformManager().register(this.platform);

        this.notifyReady = true;
    }

    @EventHandler
    public void onServerStop(LifecycleStopEvent lifecycleStopEvent) {
        this.notifyReady = false;
        if (this.platform != null) {
            WorldEdit.getInstance().getPlatformManager().unregister(this.platform);
            this.platform = null;
        }
    }

    @EventHandler
    public void onGlobalTick(GlobalTickEvent globalTickEvent) {
        if (this.notifyReady) {
            this.notifyReady = false;
            WorldEdit.getInstance().getEventBus().post(new PlatformReadyEvent());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = 2000)
    public void onBlockLeftClick(PlayerStartBreakBlockEvent event) {
        if (this.platform == null || !this.platform.isHookingEvents()) {
            return;
        }
        WorldEdit we = WorldEdit.getInstance();
        FoxLoaderPlayer player = new FoxLoaderPlayer(event.getEntityPlayer());
        FoxLoaderWorld world = new FoxLoaderWorld(event.getEntityPlayer().worldObj);
        WorldVector pos = new WorldVector(LocalWorldAdapter.adapt(world),
                event.getX(), event.getY(), event.getZ());

        if (we.handleBlockLeftClick(player, pos)) {
            event.setCancelled(true);
        }

        if (we.handleArmSwing(player)) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = 2000)
    public void onBlockRightClick(PlayerUseItemOnBlockEvent event) {
        if (this.platform == null || !this.platform.isHookingEvents()) {
            return;
        }

        WorldEdit we = WorldEdit.getInstance();
        FoxLoaderPlayer player = new FoxLoaderPlayer(event.getEntityPlayer());
        FoxLoaderWorld world = new FoxLoaderWorld(event.getEntityPlayer().worldObj);
        WorldVector pos = new WorldVector(LocalWorldAdapter.adapt(world),
                event.getX(), event.getY(), event.getZ());

        if (we.handleBlockRightClick(player, pos)) {
            event.setCancelled(true);
        }

        if (we.handleRightClick(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = 2000)
    public void onBlockRightClick(PlayerUseItemOnAirEvent event) {
        if (this.platform == null || !this.platform.isHookingEvents()) {
            return;
        }

        WorldEdit we = WorldEdit.getInstance();
        FoxLoaderPlayer player = new FoxLoaderPlayer(event.getEntityPlayer());

        if (we.handleRightClick(player)) {
            event.setCancelled(true);
        }
    }


    public static ItemStack toFoxLoaderItemStack(BaseItemStack item) {
        return new ItemStack(item.getType(), item.getAmount(), item.getData());
    }
}
