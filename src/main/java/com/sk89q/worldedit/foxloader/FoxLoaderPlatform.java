package com.sk89q.worldedit.foxloader;

import com.fox2code.foxloader.launcher.FoxLauncher;
import com.fox2code.foxloader.loader.ModLoaderInit;
import com.fox2code.foxloader.registry.CommandRegistry;
import com.fox2code.foxloader.registry.GameRegistry;
import com.fox2code.foxloader.registry.missing.MissingItem;
import com.fox2code.foxloader.registry.missing.MissingItemBlock;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.*;
import com.sk89q.worldedit.util.command.CommandMapping;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.common.entity.EntityList;
import net.minecraft.common.entity.player.EntityPlayer;
import net.minecraft.common.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.world.WorldServer;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FoxLoaderPlatform extends AbstractPlatform implements MultiUserPlatform {
    private final FoxLoaderWorldEdit mod;
    private boolean hookingEvents = false;

    public FoxLoaderPlatform(FoxLoaderWorldEdit mod) {
        this.mod = mod;
    }

    boolean isHookingEvents() {
        return hookingEvents;
    }

    @Override
    public int resolveItem(String name) {
        Item item = Item.getItemByName(name);
        return item == null || item instanceof MissingItem ||
                item instanceof MissingItemBlock ? -1 : item.itemID;
    }

    @Override
    public boolean isValidMobType(String type) {
        return EntityList.getEntityTypeNames().contains(type);
    }

    @Override
    public void reload() {
        getConfiguration().load();
    }

    @Override
    public List<? extends World> getWorlds() {
        return super.getWorlds();
    }

    @Override
    public @Nullable Player matchPlayer(Player player) {
        if (player instanceof FoxLoaderPlayer) {
            return player;
        } else {
            if (FoxLauncher.isClient()) {
                EntityPlayer entity = Minecraft.getInstance().thePlayer;
                return entity != null && entity.username.equals(player.getName()) ?
                        new FoxLoaderPlayer(entity) : null;
            }

            EntityPlayer entity = MinecraftServer.getInstance().findPlayer(player.getName());
            return entity != null ? new FoxLoaderPlayer(entity) : null;
        }
    }

    @Override
    public @Nullable World matchWorld(World world) {
        if (world instanceof FoxLoaderWorld) {
            return world;
        } else {
            for (WorldServer ws : MinecraftServer.getInstance().worldMngr) {
                if (FoxLoaderWorld.getWorldName(ws).equals(world.getName())) {
                    return new FoxLoaderWorld(ws);
                }
            }

            return null;
        }
    }

    @Override
    public void registerCommands(Dispatcher dispatcher) {
        for (final CommandMapping command : dispatcher.getCommands()) {
            CommandWrapper.debugMissing(command);
        }
    }

    @Override
    public void registerGameHooks() {
        hookingEvents = true;
    }

    @Override
    public LocalConfiguration getConfiguration() {
        return this.mod.getConfig();
    }

    @Override
    public String getVersion() {
        return "6.1.11";
    }

    @Override
    public String getPlatformName() {
        return "FoxLoader-Unofficial";
    }

    @Override
    public String getPlatformVersion() {
        return this.mod.getModContainer().getModInfo().version;
    }

    @Override
    public Map<Capability, Preference> getCapabilities() {
        Map<Capability, Preference> capabilities = new EnumMap<>(Capability.class);
        capabilities.put(Capability.CONFIGURATION, Preference.PREFER_OTHERS);
        capabilities.put(Capability.WORLDEDIT_CUI, Preference.NORMAL);
        capabilities.put(Capability.GAME_HOOKS, Preference.NORMAL);
        capabilities.put(Capability.PERMISSIONS, Preference.PREFER_OTHERS);
        capabilities.put(Capability.USER_COMMANDS, Preference.NORMAL);
        capabilities.put(Capability.WORLD_EDITING, Preference.PREFERRED);
        return capabilities;
    }

    @Override
    public Collection<Actor> getConnectedUsers() {
        if (FoxLauncher.isClient()) {
            EntityPlayer entityPlayer = Minecraft.getInstance().thePlayer;
            return entityPlayer == null ? Collections.emptyList() :
                    Collections.singleton(new FoxLoaderPlayer(entityPlayer));
        }

        List<EntityPlayerMP> playerEntities =
                MinecraftServer.getInstance().configManager.playerEntities;
        List<Actor> users = new ArrayList<>(playerEntities.size());
        for (EntityPlayerMP player : playerEntities) {
            if (player != null) {
                users.add(new FoxLoaderPlayer(player));
            }
        }
        return users;
    }
}
