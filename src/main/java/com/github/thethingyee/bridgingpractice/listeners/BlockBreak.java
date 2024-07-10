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
        if(bridgingPractice.getActiveSessions().get(e.getPlayer()).isSpectatingPlayer() && e.getPlayer().getWorld().equals(bridgingPractice.getActiveSessions().get(e.getPlayer()).getSpectating().getWorld())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't break blocks here!");
            return;
        }

        if (e.getPlayer().getWorld().equals(bridgingPractice.getActiveSessions().get(e.getPlayer()).getAssignedWorld())) {
            if(!e.getBlock().getType().name().toUpperCase().endsWith("WOOL")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You are not allowed to break blocks other than Wool.");
            }
        }
    }
}
