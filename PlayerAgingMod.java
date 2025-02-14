package com.cody.playeragingmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Mod(modid = PlayerAgingMod.MODID, name = PlayerAgingMod.NAME, version = PlayerAgingMod.VERSION)
public class PlayerAgingMod {
    public static final String MODID = "playeragingmod";
    public static final String NAME = "Player Aging Mod";
    public static final String VERSION = "1.0";

    private static final ConcurrentHashMap<UUID, Integer> playerAges = new ConcurrentHashMap<>();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Player Aging Mod Initialized!");
        MinecraftForge.EVENT_BUS.register(new RenderPlayerScaler());
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        MinecraftForge.EVENT_BUS.register(new TimeManager());
        MinecraftForge.EVENT_BUS.register(new PlayerTickHandler());

        ClientRegistry.registerKeyBinding(KeyInputHandler.openGuiKey);
    }

    public static int getPlayerAge(UUID playerUUID) {
        return playerAges.getOrDefault(playerUUID, 0);
    }

    public static void setPlayerAge(UUID playerUUID, int age) {
        playerAges.put(playerUUID, age);
    }
}
