package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.GUIManager;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.github.thethingyee.bridgingpractice.utils.Session;
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
        GUIManager guiManager = bridgingPractice.getGuiManager();
        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;
        Session currentSession = bridgingPractice.getActiveSessions().get(e.getPlayer());
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {
            if(e.getPlayer().getItemInHand().getItemMeta() == null) return;
            if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getChangeColMeta())) {
                guiManager.changeColorGUI(e.getPlayer());
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "You have opened the Change Color GUI.");
            } else if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getRestartMeta())) {
                if (e.getPlayer().getWorld().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {
                    double[] offset = Offsets.getOffsets(currentSession.getSchematicName());
                    e.getPlayer().teleport(new Location(Bukkit.getWorld(e.getPlayer().getUniqueId().toString().replaceAll("-", "")), 0.0, 128.0, 0.0).add(offset[0], offset[1]+1, offset[2]));
                    if (currentSession.getBlockPlaced() != null) {
                        for (Location loc : currentSession.getBlockPlaced()) {
                            e.getPlayer().getWorld().getBlockAt(loc).setType(Material.AIR);
                        }
                    }
                    currentSession.setBlocksPlaced(0);
                    e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Successfully restarted world.");

                }
            } else if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getLeaveMeta())) {
                if (currentSession.getAssignedWorld() != null) {
                    e.getPlayer().teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                    String worldName = e.getPlayer().getUniqueId().toString().replaceAll("-", "");
                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        if(!world.getPlayers().isEmpty()) {
                            for(Player player : world.getPlayers()) {
                                if(bridgingPractice.getActiveSessions().get(player).getSpectating() == e.getPlayer()) {
                                    currentSession.getScoreboard().delete();
                                    currentSession.setScoreboard(null);
                                    currentSession.setSpectating(null);
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
                        currentSession.setAssignedWorld(null);
                        currentSession.setSchematicName(null);
                        bridgingPractice.getActiveSessions().remove(e.getPlayer());
                        bridgingPractice.getWorldArray().remove(worldName);
                    }
                }
                e.getPlayer().getInventory().clear();
            } else if(e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getRefillMeta())) {
                e.getPlayer().getInventory().clear();
                guiManager.giveInventoryItems(e.getPlayer(), currentSession.getWoolColor());
            }
        }
        if(currentSession.isSpectatingPlayer()) {
            if(currentSession.getSpectating().getWorld() == e.getPlayer().getWorld()) {
                if(e.getPlayer().getItemInHand().getItemMeta() == null) return;
                if(e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getSpectatorLeave())) {
                    currentSession.getScoreboard().delete();
                    currentSession.setScoreboard(null);
                    currentSession.setSpectating(null);
                    e.getPlayer().teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                    e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Teleported!");
                    e.getPlayer().getInventory().clear();
                }
            }
        }
    }
}
