package com.cody.playeragingmod;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public class GuiPlayerInfo extends GuiScreen {
    private boolean showCalendar = false;
    private static final int GROW_UP_DAYS = 52;

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 50, height - 40, 100, 20, "Toggle Calendar"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            showCalendar = !showCalendar; // Toggle calendar state
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Player Information", width / 2, 20, 0xFFFFFF);

        EntityPlayer player = Minecraft.getMinecraft().player;
        int daysLived = PlayerAgingMod.getPlayerAge(player.getUniqueID());
        int realYears = 6 + (daysLived * 2 / GROW_UP_DAYS);

        String growthStage;
        if (daysLived < 18) {
            growthStage = "Child";
        } else if (daysLived < 35) {
            growthStage = "Teen";
        } else if (daysLived < GROW_UP_DAYS) {
            growthStage = "Adult";
        } else {
            growthStage = "Mature Adult";
        }

        if (!showCalendar) {
            // Display player stats when NOT in calendar mode
            drawString(fontRenderer, "Name: " + player.getName(), 20, 50, 0xFFFFFF);
            drawString(fontRenderer, "Time Lived: " + daysLived + " Days", 20, 70, 0xFFFFFF);
            drawString(fontRenderer, "Real Years Converted: " + realYears + " Years", 20, 90, 0xFFFFFF);
            drawString(fontRenderer, "Growth Stage: " + growthStage, 20, 110, 0xFFFFFF);
        } else {
            // Draw the calendar date when toggled ON
            String currentDate = TimeManager.getCurrentDate();
            drawCenteredString(fontRenderer, "Calendar Date:", width / 2, 50, 0xFFFFFF);
            drawCenteredString(fontRenderer, currentDate, width / 2, 70, 0xFFFF55);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
