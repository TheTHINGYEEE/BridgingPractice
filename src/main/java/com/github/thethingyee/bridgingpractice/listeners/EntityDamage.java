package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.github.thethingyee.bridgingpractice.utils.WoolColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

    private final BridgingPractice bridgingPractice;

    public EntityDamage(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        HMaps hMaps = bridgingPractice.gethMaps();
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.getWorld().getName().equalsIgnoreCase(player.getUniqueId().toString().replaceAll("-", ""))) {
                if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                    player.teleport(new Location(Bukkit.getWorld(player.getUniqueId().toString().replaceAll("-", "")), 0.0, 128.0, 0.0)
                            .add(Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[0], Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[1]+1, Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[2]));
                    if(hMaps.getBlockPlaced().containsKey(player)) {
                        for (Location loc : hMaps.getBlockPlaced().get(player)) {
                            player.getWorld().getBlockAt(loc).setType(Material.AIR);
                        }
                    }
                    player.getInventory().clear();
                    bridgingPractice.getGuiManager().giveInventoryItems(player, hMaps.getSelectedWoolColor().get(player), WoolColor.woolToDye(hMaps.getSelectedWoolColor().get(player)));

                    hMaps.getBlockPlaced().remove(player);
                    hMaps.getBlocksPlaced().put(player, 0);
                    player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "Oops! You fell down! Restarting..");
                }
                e.setCancelled(true);
            }
            if(!player.getWorld().equals(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")))) {
                if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                    player.teleport(new Location(player.getWorld(), 0.0, 128.0, 0.0)
                            .add(Offsets.getOffsets(hMaps.getPlayerSchematic().get(hMaps.getSpectatingPlayer().get(player)))[0], Offsets.getOffsets(hMaps.getPlayerSchematic().get(hMaps.getSpectatingPlayer().get(hMaps.getSpectatingPlayer().get(player))))[1]+1, Offsets.getOffsets(hMaps.getPlayerSchematic().get(hMaps.getSpectatingPlayer().get(player)))[2]));
                    player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "You fell down! Putting back to spawn.");
                }
                e.setCancelled(true);
            }
        }
    }
}
