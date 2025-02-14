package com.cody.playeragingmod;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTickHandler {
    private static final Map<UUID, Integer> playerAges = new ConcurrentHashMap<>();
    private static final int TICKS_PER_DAY = 24000;

    // ✅ Instead of onPlayerLoggedIn, we check active players every world tick
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isRemote) {
            for (EntityPlayer player : event.world.playerEntities) {
                UUID playerUUID = player.getUniqueID();
                playerAges.putIfAbsent(playerUUID, 0);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.world.isRemote) {
            EntityPlayer player = event.player;
            UUID playerUUID = player.getUniqueID();

            int currentTicks = playerAges.getOrDefault(playerUUID, 0) + 1;
            playerAges.put(playerUUID, currentTicks);

            if (currentTicks % TICKS_PER_DAY == 0) {
                System.out.println("Player " + player.getName() + " has lived another Minecraft day.");
            }
        }
    }

    // ✅ Instead of PlayerLoggedOutEvent, we clean up inactive players every server tick
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (server == null) return;

            playerAges.keySet().removeIf(playerUUID -> {
                EntityPlayer loggedOutPlayer = server.getPlayerList().getPlayerByUUID(playerUUID);
                return loggedOutPlayer == null;
            });
        }
    }

    public static int getPlayerAge(UUID playerUUID) {
        return playerAges.getOrDefault(playerUUID, 0) / TICKS_PER_DAY;
    }
}
