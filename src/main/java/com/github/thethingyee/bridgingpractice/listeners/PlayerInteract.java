package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.GUIManager;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.github.thethingyee.bridgingpractice.utils.WoolColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.IOException;

public class PlayerInteract implements Listener {

    private final BridgingPractice bridgingPractice;

    public PlayerInteract(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        HMaps hMaps = bridgingPractice.gethMaps();
        GUIManager guiManager = bridgingPractice.getGuiManager();
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {
            if(e.getPlayer().getItemInHand().getItemMeta() == null) return;
            if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getChangeColMeta())) {
                guiManager.changeColorGUI(e.getPlayer());
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "You have opened the Change Color GUI.");
            } else if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getRestartMeta())) {
                if (e.getPlayer().getWorld().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {

                    e.getPlayer().teleport(new Location(Bukkit.getWorld(e.getPlayer().getUniqueId().toString().replaceAll("-", "")), 0.0, 128.0, 0.0).add(Offsets.getOffsets(hMaps.getPlayerSchematic().get(e.getPlayer()))[0], Offsets.getOffsets(hMaps.getPlayerSchematic().get(e.getPlayer()))[1]+1, Offsets.getOffsets(hMaps.getPlayerSchematic().get(e.getPlayer()))[2]));
                    if (hMaps.getBlockPlaced().containsKey(e.getPlayer())) {
                        for (Location loc : hMaps.getBlockPlaced().get(e.getPlayer())) {
                            e.getPlayer().getWorld().getBlockAt(loc).setType(Material.AIR);
                        }
                    }
                    hMaps.getBlocksPlaced().put(e.getPlayer(), 0);
                    e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Successfully restarted world.");

                }
            } else if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getLeaveMeta())) {
                if (hMaps.getAssignedWorld().containsKey(e.getPlayer())) {
                    e.getPlayer().teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                    String worldName = e.getPlayer().getUniqueId().toString().replaceAll("-", "");
                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        if(!world.getPlayers().isEmpty()) {
                            for(Player player : world.getPlayers()) {
                                if(hMaps.getSpectatingPlayer().get(player) == e.getPlayer()) {
                                    hMaps.getPlayerScoreboard().get(player).delete();
                                    hMaps.getPlayerScoreboard().remove(player);
                                    hMaps.getSpectatingPlayer().remove(player);
                                    player.setAllowFlight(false);
                                    player.setFlying(false);
                                    player.getInventory().clear();
                                }
                                player.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                                e.getPlayer().showPlayer(player);
                            }
                        }
                        Bukkit.unloadWorld(world, false);
                        try {
                            FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + "/" + worldName));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        Bukkit.getConsoleSender().sendMessage(bridgingPractice.prefix + "Deleted world '" + worldName + "'");
                        hMaps.getAssignedWorld().remove(e.getPlayer());
                        hMaps.getPlayerSchematic().remove(e.getPlayer());
                        bridgingPractice.getWorldArray().remove(worldName);
                    }
                }
                e.getPlayer().getInventory().clear();
            } else if(e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getRefillMeta())) {
                e.getPlayer().getInventory().clear();
                guiManager.giveInventoryItems(e.getPlayer(), hMaps.getSelectedWoolColor().get(e.getPlayer()), WoolColor.woolToDye(hMaps.getSelectedWoolColor().get(e.getPlayer())));
            }
        }
        if(hMaps.getSpectatingPlayer().containsKey(e.getPlayer())) {
            if(hMaps.getSpectatingPlayer().get(e.getPlayer()).getWorld() == e.getPlayer().getWorld()) {
                if(e.getPlayer().getItemInHand().getItemMeta() == null) return;
                if(e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getSpectatorLeave())) {
                    hMaps.getPlayerScoreboard().get(e.getPlayer()).delete();
                    hMaps.getPlayerScoreboard().remove(e.getPlayer());
                    hMaps.getSpectatingPlayer().remove(e.getPlayer());
                    e.getPlayer().teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                    e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Teleported!");
                    e.getPlayer().getInventory().clear();
                }
            }
        }
    }
}
