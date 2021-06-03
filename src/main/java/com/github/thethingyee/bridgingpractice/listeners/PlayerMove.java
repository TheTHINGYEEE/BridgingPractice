package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.github.thethingyee.bridgingpractice.utils.WoolColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    private final BridgingPractice bridgingPractice;

    public PlayerMove(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        HMaps hMaps = bridgingPractice.gethMaps();
        if(e.getPlayer().getWorld().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {
            if (e.getPlayer().getLocation().getBlockY() <= bridgingPractice.getConfig().getInt("defaults.kill-zone")) {
                e.getPlayer().teleport(new Location(Bukkit.getWorld(e.getPlayer().getUniqueId().toString().replaceAll("-", "")), 0.0, 128.0, 0.0).add(Offsets.getOffsets(hMaps.getPlayerSchematic().get(e.getPlayer()))[0], Offsets.getOffsets(hMaps.getPlayerSchematic().get(e.getPlayer()))[1] + 1, Offsets.getOffsets(hMaps.getPlayerSchematic().get(e.getPlayer()))[2]));
                if (hMaps.getBlockPlaced().containsKey(e.getPlayer())) {
                    for (Location loc : hMaps.getBlockPlaced().get(e.getPlayer())) {
                        e.getPlayer().getWorld().getBlockAt(loc).setType(Material.AIR);
                    }
                }
                e.getPlayer().getInventory().clear();
                bridgingPractice.getGuiManager().giveInventoryItems(e.getPlayer(), hMaps.getSelectedWoolColor().get(e.getPlayer()), WoolColor.woolToDye(hMaps.getSelectedWoolColor().get(e.getPlayer())));
                hMaps.getBlockPlaced().remove(e.getPlayer());
                hMaps.getBlocksPlaced().put(e.getPlayer(), 0);
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "Oops! You fell down! Restarting..");
            }
        }
        if(!e.getPlayer().getWorld().equals(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")))) {
            if (e.getPlayer().getLocation().getBlockY() <= bridgingPractice.getConfig().getInt("defaults.kill-zone")) {
                Location loc = new Location(e.getPlayer().getWorld(), 0.0, 128.0, 0.0).add(Offsets.getOffsets(hMaps.getPlayerSchematic().get(hMaps.getSpectatingPlayer().get(e.getPlayer())))[0], Offsets.getOffsets(hMaps.getPlayerSchematic().get(hMaps.getSpectatingPlayer().get(e.getPlayer())))[1] + 1, Offsets.getOffsets(hMaps.getPlayerSchematic().get(hMaps.getSpectatingPlayer().get(e.getPlayer())))[2]);
                e.getPlayer().teleport(loc);
                e.getPlayer().sendMessage(loc.toString());
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You fell down! Putting back to spawn.");
            }
        }
    }
}
