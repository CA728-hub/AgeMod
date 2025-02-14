package com.cody.playeragingmod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class RenderPlayerScaler {

    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        UUID playerUUID = player.getUniqueID();
        int ageInDays = PlayerAgingMod.getPlayerAge(playerUUID);

        float maxAge = 52.0f;
        float startScale = 0.5f;
        float maxScale = 1.0f;

        float growthFactor = Math.min((float) ageInDays / maxAge, 1.0f);
        float scale = startScale + (maxScale - startScale) * growthFactor;

        scale = Math.max(scale, 0.5f);

        player.eyeHeight = player.getDefaultEyeHeight() * scale;

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
    }

    @SubscribeEvent
    public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        GlStateManager.popMatrix();
    }
}
