package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.Session;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class BlockPlace implements Listener {

    private final BridgingPractice bridgingPractice;

    public BlockPlace(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(e.getPlayer().getWorld().getName().equalsIgnoreCase(bridgingPractice.getConfig().getString("defaults.world"))) return;

        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;
        Session currentSession = bridgingPractice.getActiveSessions().get(e.getPlayer());
        if(currentSession.getSpectating() != null) {
            if(currentSession.getSpectating().getWorld().getName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't place blocks here!");
            }
        }

        if(e.getPlayer().getWorld().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {
            if (e.getBlockPlaced().getType().equals(Material.WOOL)) {
                if (currentSession.getBlockPlaced() != null) {
                    currentSession.getBlockPlaced().add(new Location(e.getPlayer().getWorld(), e.getBlockPlaced().getLocation().getBlockX(), e.getBlockPlaced().getLocation().getBlockY(), e.getBlockPlaced().getLocation().getBlockZ()));
                    int placedBlocks = currentSession.getBlocksPlaced();
                    currentSession.setBlocksPlaced(placedBlocks + 1);
                } else {
                    currentSession.setBlockPlaced(new ArrayList<>());
                    currentSession.setBlocksPlaced(1);
                    currentSession.getBlockPlaced().add(new Location(e.getPlayer().getWorld(), e.getBlockPlaced().getLocation().getBlockX(), e.getBlockPlaced().getLocation().getBlockY(), e.getBlockPlaced().getLocation().getBlockZ()));
                    bridgingPractice.getPlayerSpeed().startPlayerSpeed(e.getPlayer());
                }
            } else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can only place WOOL here.");
            }
        } else {
            e.setCancelled(true);
            e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't place blocks here!");
        }
    }
}
