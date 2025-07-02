package com.sk89q.worldedit.foxloader;

import com.fox2code.foxloader.launcher.FoxLauncher;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.common.command.ICommandListener;
import net.minecraft.common.entity.player.EntityPlayer;
import net.minecraft.common.item.ItemStack;
import net.minecraft.common.util.ChatColors;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ConcurrentModificationException;
import java.util.Locale;
import java.util.UUID;

public class FoxLoaderPlayer extends AbstractPlayerActor {
    private final EntityPlayer player;
    private final ICommandListener commandListener;
    private final UUID offlineUUID;

    public FoxLoaderPlayer(EntityPlayer player) {
        this.player = player;
        if (player instanceof EntityPlayerMP entityPlayerMP) {
            this.commandListener = entityPlayerMP.playerNetServerHandler;
        } else if (player instanceof ICommandListener playerCommandListener) {
            this.commandListener = playerCommandListener;
        } else {
            throw new RuntimeException("Invalid player!");
        }
        this.offlineUUID = UUID.nameUUIDFromBytes(
                this.player.username.toLowerCase(Locale.ROOT)
                        .getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public World getWorld() {
        return new FoxLoaderWorld(this.player.worldObj);
    }

    @Override
    public int getItemInHand() {
        ItemStack itemStack = this.player.getHeldItem();
        return itemStack == null ? 0 : itemStack.getItemID();
    }

    @Override
    public void giveItem(int type, int amount) {

    }

    @Override
    public BlockBag getInventoryBlockBag() {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public WorldVector getPosition() {
        return new WorldVector(LocalWorldAdapter.adapt(new FoxLoaderWorld(this.player.worldObj)),
                this.player.posX, this.player.posY, this.player.posZ);
    }

    @Override
    public double getPitch() {
        return this.player.rotationPitch;
    }

    @Override
    public double getYaw() {
        return this.player.rotationYaw;
    }

    @Override
    public void setPosition(Vector pos, float pitch, float yaw) {
        this.player.teleportTo(pos.getX(), pos.getY(), pos.getZ(), pitch, yaw);
    }

    @Override
    public @Nullable BaseEntity getState() {
        return null;
    }

    @Override
    public Location getLocation() {
        Vector position = new Vector(this.player.posX, this.player.posY, this.player.posZ);
        float yaw = this.player.rotationYaw;
        float pitch = this.player.rotationPitch;

        return new Location(new FoxLoaderWorld(this.player.worldObj), position, yaw, pitch);
    }

    @Override
    public String getName() {
        return this.player.username;
    }

    @Override
    public void printRaw(String msg) {
        sendColorized(msg, ChatColors.RESET);
    }

    @Override
    public void printDebug(String msg) {
        sendColorized(msg, ChatColors.GRAY);
    }

    @Override
    public void print(String msg) {
        sendColorized(msg, ChatColors.LIGHT_PURPLE);
    }

    @Override
    public void printError(String msg) {
        sendColorized(msg, ChatColors.RED);
    }

    private void sendColorized(String msg, String formatting) {
        for (String part : msg.split("\n")) {
            this.commandListener.log(formatting + part);
        }
    }

    @Override
    public SessionKey getSessionKey() {
        return new SessionKeyImpl(this.offlineUUID, this.player.username);
    }

    @Override
    public @Nullable <T> T getFacet(Class<? extends T> cls) {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return this.offlineUUID;
    }

    @Override
    public String[] getGroups() {
        return new String[0];
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.isOp();
    }

    @Override
    public boolean hasCreativeMode() {
        return this.player.capabilities.isCreativeMode;
    }

    private static class SessionKeyImpl implements SessionKey {
        // If not static, this will leak a reference

        private final UUID uuid;
        private final String name;

        private SessionKeyImpl(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }

        @Override
        public UUID getUniqueId() {
            return this.uuid;
        }

        @Nullable
        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isActive() {
            if (FoxLauncher.isClient()) {
                EntityPlayer entityPlayer = Minecraft.theMinecraft.thePlayer;
                return entityPlayer != null && entityPlayer.username.equals(this.name);
            }
            if (!MinecraftServer.isServerRunning(MinecraftServer.getInstance())) {
                return false;
            }
            while (true) {
                try {
                    return MinecraftServer.getInstance().findPlayer(this.name) != null;
                } catch (ConcurrentModificationException ignored) {}
            }
        }

        @Override
        public boolean isPersistent() {
            return true;
        }

    }
}
