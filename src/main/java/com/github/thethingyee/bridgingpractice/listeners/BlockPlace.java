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
        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;
        if(e.getPlayer().getWorld().getName().equalsIgnoreCase(bridgingPractice.getConfig().getString("defaults.world"))) return;

        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;
        Session currentSession = bridgingPractice.getActiveSessions().get(e.getPlayer());

        // if player spectating player
        if(currentSession.getSpectating() != null && currentSession.getSpectating().getWorld().getName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't place blocks here!");
            return;
        }

        if(!e.getPlayer().getWorld().equals(bridgingPractice.getActiveSessions().get(e.getPlayer()).getAssignedWorld())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't place blocks here!");
            return;
        }

        if (!e.getBlockPlaced().getType().equals(Material.WOOL)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can only place WOOL here.");
            return;
        }
        if (currentSession.getBlockPlaced() == null) {
            currentSession.setBlockPlaced(new ArrayList<>());
            currentSession.setBlocksPlaced(1);
            currentSession.getBlockPlaced().add(new Location(e.getPlayer().getWorld(), e.getBlockPlaced().getLocation().getBlockX(), e.getBlockPlaced().getLocation().getBlockY(), e.getBlockPlaced().getLocation().getBlockZ()));
            return;
        }

        currentSession.getBlockPlaced().add(new Location(e.getPlayer().getWorld(), e.getBlockPlaced().getLocation().getBlockX(), e.getBlockPlaced().getLocation().getBlockY(), e.getBlockPlaced().getLocation().getBlockZ()));
        int placedBlocks = currentSession.getBlocksPlaced();
        currentSession.setBlocksPlaced(placedBlocks + 1);
    }
}
