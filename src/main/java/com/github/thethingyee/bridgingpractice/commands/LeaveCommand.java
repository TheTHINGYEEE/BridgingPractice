package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class LeaveCommand extends Command {

    public LeaveCommand(BridgingPractice bridgingPractice) {
        super(bridgingPractice);
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public boolean performOnlyAtSession() {
        return true;
    }

    @Override
    public void perform(Player player, String[] args) {

        if(bridgingPractice.getActiveSessions().get(player).getAssignedWorld() != null) {
            player.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
            String worldName = player.getUniqueId().toString().replaceAll("-", "");
            World world = Bukkit.getWorld(worldName);
            if(world != null) {
                if(!world.getPlayers().isEmpty()) {
                    for(Player pl : world.getPlayers()) {
                        if(bridgingPractice.getActiveSessions().get(pl).getSpectating() == player) {
                            bridgingPractice.getActiveSessions().get(pl).setSpectating(null);
                        }
                        pl.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                        player.showPlayer(pl);
                    }
                }
                Bukkit.unloadWorld(world, false);
                try {
                    FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + "/" + worldName));
                } catch (IOException ex) {
                    Bukkit.getConsoleSender().sendMessage("Exception found! " + ex.getMessage());

                }
                Bukkit.getConsoleSender().sendMessage("Deleted world '" + worldName + "'");
//                                    hMaps.getAssignedWorld().remove(player);
                bridgingPractice.getActiveSessions().get(player).setAssignedWorld(null);
                bridgingPractice.getWorldArray().remove(worldName);
                bridgingPractice.getActiveSessions().get(player).getScoreboard().delete();
                bridgingPractice.getActiveSessions().get(player).setScoreboard(null);
                bridgingPractice.getActiveSessions().remove(player);

                player.sendMessage(bridgingPractice.prefix + ChatColor.YELLOW + "Successfully left bridging...");
            }
        }
        player.getInventory().clear();
    }
}
