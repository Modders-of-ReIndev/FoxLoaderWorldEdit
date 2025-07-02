package com.sk89q.worldedit.foxloader;

import com.fox2code.foxloader.selection.PlayerSelection;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.common.entity.player.EntityPlayer;
import net.minecraft.common.util.Utils;
import net.minecraft.common.world.World;

import java.lang.ref.WeakReference;

public class PlayerSelectionWorldEdit extends PlayerSelection {
    WeakReference<FoxLoaderPlayer> foxLoaderPlayerWeakReference = Utils.nullWeakReference();

    public PlayerSelectionWorldEdit(EntityPlayer entityPlayer) {
        super(entityPlayer);
    }

    public FoxLoaderPlayer getFoxLoaderPlayer() {
        FoxLoaderPlayer foxLoaderPlayer = this.foxLoaderPlayerWeakReference.get();
        if (foxLoaderPlayer == null) {
            EntityPlayer entityPlayer = this.getPlayer();
            if (entityPlayer != null) {
                this.foxLoaderPlayerWeakReference = new WeakReference<>(
                        foxLoaderPlayer = new FoxLoaderPlayer(entityPlayer));
            }
        }
        return foxLoaderPlayer;
    }

    public LocalSession getLocalSession() {
        return WorldEdit.getInstance().getSessionManager().get(this.getFoxLoaderPlayer());
    }

    public Region getSelection() {
        LocalSession localSession = this.getLocalSession();
        if (localSession.isSelectionDefined(localSession.getSelectionWorld())) {
            try {
                return localSession.getSelection(localSession.getSelectionWorld());
            } catch (IncompleteRegionException ignored) {}
        }
        return null;
    }

    @Override
    public World getSelectionWorld() {
        LocalSession localSession = this.getLocalSession();
        com.sk89q.worldedit.world.World world = localSession.getSelectionWorld();
        if (world instanceof FoxLoaderWorld foxLoaderWorld) {
            return foxLoaderWorld.getWorld();
        }
        return null;
    }

    @Override
    public boolean hasSelection() {
        LocalSession localSession = this.getLocalSession();
        return localSession.isSelectionDefined(localSession.getSelectionWorld());
    }

    @Override
    public int getX1() {
        Region region = this.getSelection();
        return region == null ? 0 :
                region instanceof CuboidRegion cuboidRegion ?
                        cuboidRegion.getPos1().getBlockX() :
                region.getMinimumPoint().getBlockX();
    }

    @Override
    public int getX2() {
        Region region = this.getSelection();
        return region == null ? 0 :
                region instanceof CuboidRegion cuboidRegion ?
                        cuboidRegion.getPos2().getBlockY() :
                        region.getMaximumPoint().getBlockY();
    }

    @Override
    public int getY1() {
        Region region = this.getSelection();
        return region == null ? 0 :
                region instanceof CuboidRegion cuboidRegion ?
                        cuboidRegion.getPos1().getBlockY() :
                        region.getMinimumPoint().getBlockY();
    }

    @Override
    public int getY2() {
        Region region = this.getSelection();
        return region == null ? 0 :
                region instanceof CuboidRegion cuboidRegion ?
                        cuboidRegion.getPos2().getBlockY() :
                        region.getMaximumPoint().getBlockY();
    }

    @Override
    public int getZ1() {
        Region region = this.getSelection();
        return region == null ? 0 :
                region instanceof CuboidRegion cuboidRegion ?
                        cuboidRegion.getPos1().getBlockZ() :
                        region.getMinimumPoint().getBlockZ();
    }

    @Override
    public int getZ2() {
        Region region = this.getSelection();
        return region == null ? 0 :
                region instanceof CuboidRegion cuboidRegion ?
                        cuboidRegion.getPos2().getBlockZ() :
                        region.getMaximumPoint().getBlockZ();
    }
}
