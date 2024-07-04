package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
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
        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;
        if(bridgingPractice.getActiveSessions().get(e.getPlayer()).getSpectating() != null) {
            if(bridgingPractice.getActiveSessions().get(e.getPlayer()).getAssignedWorld().getName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
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
