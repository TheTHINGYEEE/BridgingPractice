package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private final BridgingPractice bridgingPractice;

    public BlockBreak(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        HMaps hMaps = bridgingPractice.gethMaps();

        if(hMaps.getSpectatingPlayer().containsKey(e.getPlayer())) {
            if(hMaps.getSpectatingPlayer().get(e.getPlayer()).getWorld().getName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't place blocks here!");
            }
        }

        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You are not allowed to break blocks.\n" + ChatColor.AQUA +
                    "Coming soon - Break blocks outside the island.");
        }
    }
}
