package com.github.thethingyee.bridgingpractice;

import com.github.thethingyee.bridgingpractice.utils.HMaps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSpeed {

    private final BridgingPractice bridgingPractice;

    public PlayerSpeed(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    public void startPlayerSpeed(Player player) {
        HMaps hMaps = bridgingPractice.gethMaps();
        BukkitRunnable runnable = new BukkitRunnable() {
            Location oldLoc = player.getLocation();
            @Override
            public void run() {
                Location newLoc = player.getLocation();
                double equation = newLoc.distance(oldLoc) * 2;
                hMaps.getPlayerSpeed().put(player, equation);
                oldLoc = newLoc;
            }
        };
        if(player == null) Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "NULL WTFFFF");
        if(hMaps.getPlayerRunnable().containsKey(player)) return;
        hMaps.getPlayerRunnable().put(player, runnable);
        hMaps.getPlayerRunnable().get(player).runTaskTimer(bridgingPractice, 0, 10);
    }

    public void stopPlayerSpeed(Player player) {
        HMaps hMaps = bridgingPractice.gethMaps();
        if(!hMaps.getPlayerRunnable().containsKey(player)) return;
        hMaps.getPlayerRunnable().get(player).cancel();
        hMaps.getPlayerRunnable().remove(player);
    }
}
