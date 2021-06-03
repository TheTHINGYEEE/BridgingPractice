package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
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
        HMaps hMaps = bridgingPractice.gethMaps();
        if(e.getPlayer().getWorld().getName().equalsIgnoreCase(bridgingPractice.getConfig().getString("defaults.world"))) return;

        if(hMaps.getSpectatingPlayer().containsKey(e.getPlayer())) {
            if(hMaps.getSpectatingPlayer().get(e.getPlayer()).getWorld().getName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't place blocks here!");
            }
        }

        if(e.getPlayer().getWorld().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {
            if (e.getBlockPlaced().getType().equals(Material.WOOL)) {
                if (hMaps.getBlockPlaced().containsKey(e.getPlayer())) {
                    hMaps.getBlockPlaced().get(e.getPlayer()).add(new Location(e.getPlayer().getWorld(), e.getBlockPlaced().getLocation().getBlockX(), e.getBlockPlaced().getLocation().getBlockY(), e.getBlockPlaced().getLocation().getBlockZ()));
                    int placedBlocks = hMaps.getBlocksPlaced().get(e.getPlayer());
                    hMaps.getBlocksPlaced().put(e.getPlayer(), (placedBlocks + 1));
                } else {
                    hMaps.getBlockPlaced().put(e.getPlayer(), new ArrayList<>());
                    hMaps.getBlocksPlaced().put(e.getPlayer(), 1);
                    hMaps.getBlockPlaced().get(e.getPlayer()).add(new Location(e.getPlayer().getWorld(), e.getBlockPlaced().getLocation().getBlockX(), e.getBlockPlaced().getLocation().getBlockY(), e.getBlockPlaced().getLocation().getBlockZ()));
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
