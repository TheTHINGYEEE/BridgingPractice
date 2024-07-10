package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.github.thethingyee.bridgingpractice.utils.Session;
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




        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;
        Session currentSession = bridgingPractice.getActiveSessions().get(e.getPlayer());

        Location from = e.getFrom().clone();
        Location to = e.getTo().clone();
        from.setY(0);
        to.setY(0);

        currentSession.setSpeed(from.distance(to) * 20);

        if(e.getPlayer().getWorld().equals(bridgingPractice.getActiveSessions().get(e.getPlayer()).getAssignedWorld())) {
            double[] offset = Offsets.getOffsets(bridgingPractice.getActiveSessions().get(e.getPlayer()).getSchematicName());
            if (e.getPlayer().getLocation().getBlockY() <= bridgingPractice.getConfig().getInt("defaults.kill-zone")) {
                e.getPlayer().teleport(new Location(bridgingPractice.getActiveSessions().get(e.getPlayer()).getAssignedWorld(), 0.0, 128.0, 0.0).add(offset[0], offset[1], offset[2]));
                if (currentSession.getBlockPlaced() != null) {
                    for (Location loc : currentSession.getBlockPlaced()) {
                        e.getPlayer().getWorld().getBlockAt(loc).setType(Material.AIR);
                    }
                }
                // reset everything stats and inventory
                e.getPlayer().getInventory().clear();
                bridgingPractice.getGuiManager().giveInventoryItems(e.getPlayer(), currentSession.getWoolColor());
                currentSession.setBlockPlaced(null);
                currentSession.setBlocksPlaced(0);
                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "Oops! You fell down! Restarting..");
            }
        }
//        if(!e.getPlayer().getWorld().equals(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")))) {
//            if (e.getPlayer().getLocation().getBlockY() <= bridgingPractice.getConfig().getInt("defaults.kill-zone")) {
//                double[] offset = Offsets.getOffsets(bridgingPractice.getActiveSessions().get(currentSession.getSpectating()).getSchematicName());
//                Location loc = new Location(e.getPlayer().getWorld(), 0.0, 128.0, 0.0).add(offset[0], offset[1] + 1, offset[2]);
//                e.getPlayer().teleport(loc);
//                e.getPlayer().sendMessage(loc.toString());
//                e.getPlayer().sendMessage(bridgingPractice.prefix + ChatColor.RED + "You fell down! Putting back to spawn.");
//            }
//        }
    }
}
