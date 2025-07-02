package com.sk89q.worldedit.foxloader;

import com.fox2code.foxloader.selection.PlayerSelection;
import com.fox2code.foxloader.selection.PlayerSelectionProvider;
import net.minecraft.common.entity.player.EntityPlayer;

import java.util.WeakHashMap;

public class PlayerSelectionProviderWorldEdit extends PlayerSelectionProvider {
    public static final PlayerSelectionProviderWorldEdit INSTANCE = new PlayerSelectionProviderWorldEdit();

    private final WeakHashMap<EntityPlayer, PlayerSelectionWorldEdit> playerSelections;

    private PlayerSelectionProviderWorldEdit() {
        this.playerSelections = new WeakHashMap<>();
    }

    @Override
    public PlayerSelection getPlayerSelection(EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote) {
            throw new IllegalArgumentException("Cannot get PlayerSelection of a remote player.");
        }
        return this.playerSelections.computeIfAbsent(entityPlayer, PlayerSelectionWorldEdit::new);
    }
}
