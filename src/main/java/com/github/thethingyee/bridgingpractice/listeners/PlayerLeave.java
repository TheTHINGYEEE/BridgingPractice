package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.Session;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;

// i hate you so much old thingy i have to replace all hmap garbage because of your bs i havent even tested the plugin yet if it even works before i did all this
// and now i have to make a schematic bruhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh fuck you
// i have been refactoring for 1 and a half hours fuck
public class PlayerLeave implements Listener {

    private final BridgingPractice bridgingPractice;

    public PlayerLeave(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;

        Session currentSession = bridgingPractice.getActiveSessions().get(e.getPlayer());

        if(currentSession.getAssignedWorld() != null) {
            p.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
            String worldName = p.getUniqueId().toString().replaceAll("-", "");
            World world = Bukkit.getWorld(worldName);
            if(world != null) {
                if(!world.getPlayers().isEmpty()) {
                    for(Player player : world.getPlayers()) {
                        if(bridgingPractice.getActiveSessions().get(player) == e.getPlayer()) {
                            bridgingPractice.getActiveSessions().get(player).getScoreboard().delete();
                            bridgingPractice.getActiveSessions().get(player).setScoreboard(null);
                            bridgingPractice.getActiveSessions().get(player).setSpectating(null);
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
                currentSession.setWoolColor(null);
                currentSession.setAssignedWorld(null);
                bridgingPractice.getWorldArray().remove(worldName);
                currentSession.setSchematicName(null);
            }
        }
        p.getInventory().clear();
    }
}
