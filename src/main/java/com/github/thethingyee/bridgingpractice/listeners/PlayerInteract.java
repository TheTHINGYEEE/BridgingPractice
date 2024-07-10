package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.GUIManager;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.github.thethingyee.bridgingpractice.utils.Session;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    private final BridgingPractice bridgingPractice;

    public PlayerInteract(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }


    /*
        TODO:
        1. Replace all existing world checks to e.getPlayer().getWorld().equals(bridgingPractice.getActiveSessions().get(e.getPlayer()).getAssignedWorld()) cuz getting world name is ass
     */

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        GUIManager guiManager = bridgingPractice.getGuiManager();
        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;
        Session currentSession = bridgingPractice.getActiveSessions().get(e.getPlayer());

        if (e.getPlayer().getWorld().equals(bridgingPractice.getActiveSessions().get(e.getPlayer()).getAssignedWorld())) {
            if (e.getPlayer().getItemInHand().getItemMeta() == null) return;
            if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getChangeColMeta())) {
                guiManager.changeColorGUI(e.getPlayer());
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "You have opened the Change Color GUI.");
                return;
            }
            if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getRestartMeta())) {
                double[] offset = Offsets.getOffsets(currentSession.getSchematicName());
                e.getPlayer().teleport(new Location(currentSession.getAssignedWorld(), 0.0, 128.0, 0.0).add(offset[0], offset[1] + 1, offset[2]));
                if (currentSession.getBlockPlaced() != null) {
                    for (Location loc : currentSession.getBlockPlaced()) {
                        e.getPlayer().getWorld().getBlockAt(loc).setType(Material.AIR);
                    }
                }
                currentSession.setBlocksPlaced(0);
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Successfully restarted world.");
                return;
            }
            if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getLeaveMeta())) {
                if (currentSession.getAssignedWorld() != null) {
                    if (!bridgingPractice.getActiveSessions().get(e.getPlayer()).leaveSession(bridgingPractice, e.getPlayer())) {
                        e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "Can't leave session.");
                        return;
                    }
                    e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Successfully left session...");
                }
                e.getPlayer().getInventory().clear();
            } else if (e.getPlayer().getItemInHand().getItemMeta().equals(guiManager.getRefillMeta())) {
                e.getPlayer().getInventory().clear();
                guiManager.giveInventoryItems(e.getPlayer(), currentSession.getWoolColor());
                return;
            }
            return;
        }

        if(currentSession.isSpectatingPlayer() && currentSession.getSpectating().getWorld() == e.getPlayer().getWorld()) {
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
