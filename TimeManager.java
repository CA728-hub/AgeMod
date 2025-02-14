package com.cody.playeragingmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class TimeManager {
    private static final String[] DAYS = {"Wolfsday", "Blazeday", "Witherday", "Ghastday", "Enderday"};
    private static final String[] MONTHS = {"Redstonary", "Gol'dar", "Diamondar", "Emeraldary"};
    private static final int[] DAYS_IN_MONTH = {12, 13, 9, 12};

    private static int currentDay = 1;
    private static int currentMonth = 0;
    private static int currentYear = 0;
    private static int dayOfWeek = 0; // Tracks which weekday it is

    @SubscribeEvent
    public void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (!event.getEntityPlayer().world.isRemote) {
            advanceDay(event.getEntityPlayer());
        }
    }

    private void advanceDay(EntityPlayer player) {
        UUID playerUUID = player.getUniqueID();
        int currentDaysLived = PlayerAgingMod.getPlayerAge(playerUUID);
        PlayerAgingMod.setPlayerAge(playerUUID, currentDaysLived + 1);

        // ✅ Increase day count
        currentDay++;
        dayOfWeek = (dayOfWeek + 1) % DAYS.length; // Cycle through weekdays

        // ✅ Check if the month needs to change
        if (currentDay > DAYS_IN_MONTH[currentMonth]) {
            currentDay = 1; // Reset day
            currentMonth++; // Move to next month

            // ✅ Check if the year needs to change
            if (currentMonth >= MONTHS.length) {
                currentMonth = 0; // Reset to first month
                currentYear++; // Increase year
            }
        }

        // ✅ Notify player
        player.sendMessage(new TextComponentString("New Day: " + getCurrentDate()));
    }

    public static String getCurrentDate() {
        return DAYS[dayOfWeek] + " on " + currentDay + " of " + MONTHS[currentMonth] + " in Year " + currentYear;
    }
}
