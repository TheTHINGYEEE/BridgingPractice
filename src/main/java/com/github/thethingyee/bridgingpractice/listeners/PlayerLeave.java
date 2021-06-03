package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;

public class PlayerLeave implements Listener {

    private final BridgingPractice bridgingPractice;

    public PlayerLeave(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        HMaps hMaps = bridgingPractice.gethMaps();
        Player p = e.getPlayer();
        if(hMaps.getAssignedWorld().containsKey(p)) {
            p.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
            String worldName = p.getUniqueId().toString().replaceAll("-", "");
            World world = Bukkit.getWorld(worldName);
            if(world != null) {
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
                        p.showPlayer(player);
                    }
                }
                Bukkit.unloadWorld(world, false);
                try {
                    FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + "/" + worldName));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Bukkit.getConsoleSender().sendMessage(bridgingPractice.prefix + "Deleted world '" + worldName + "'");
                bridgingPractice.getPlayerSpeed().stopPlayerSpeed(e.getPlayer());
                hMaps.getSelectedWoolColor().remove(e.getPlayer());
                hMaps.getAssignedWorld().remove(p);
                bridgingPractice.getWorldArray().remove(worldName);
                hMaps.getPlayerSchematic().remove(p);
            }
        }
        p.getInventory().clear();
    }
}
