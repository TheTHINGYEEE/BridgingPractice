package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
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

        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if(!bridgingPractice.getActiveSessions().containsKey(player)) return;
        if (player.getWorld().equals(bridgingPractice.getActiveSessions().get(player).getAssignedWorld())) {
            if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) teleportPlayerToSpawn(player);
            e.setCancelled(true);
        }
        // irdk wtf is this for but ig it's for spectating?
        // another one is also on playermoveevent

//        if(!player.getWorld().equals(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")))) {
//            if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
//                double[] offset = Offsets.getOffsets(bridgingPractice.getActiveSessions().get(bridgingPractice.getActiveSessions().get(player).getSpectating()).getSchematicName());
//                player.teleport(new Location(player.getWorld(), 0.0, 128.0, 0.0)
//                        .add(offset[0], offset[1]+1, offset[2]));
//                player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "You fell down! Putting back to spawn.");
//            }
//            e.setCancelled(true);
//        }

    }

    private void teleportPlayerToSpawn(Player player) {
        double[] offset = Offsets.getOffsets(bridgingPractice.getActiveSessions().get(player).getSchematicName());
        player.teleport(new Location(bridgingPractice.getActiveSessions().get(player).getAssignedWorld(), 0.0, 128.0, 0.0)
                .add(offset[0], offset[1], offset[2]));
        if(bridgingPractice.getActiveSessions().get(player).getBlockPlaced() != null) {
            for (Location loc : bridgingPractice.getActiveSessions().get(player).getBlockPlaced()) {
                player.getWorld().getBlockAt(loc).setType(Material.AIR);
            }
        }
        player.getInventory().clear();
        bridgingPractice.getGuiManager().giveInventoryItems(player, bridgingPractice.getActiveSessions().get(player).getWoolColor());

        bridgingPractice.getActiveSessions().get(player).setBlockPlaced(null);
        bridgingPractice.getActiveSessions().get(player).setBlocksPlaced(0);
        player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "Oops! You fell down! Restarting..");
    }
}
