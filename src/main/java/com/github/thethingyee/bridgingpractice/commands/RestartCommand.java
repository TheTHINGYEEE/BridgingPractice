package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.github.thethingyee.bridgingpractice.utils.Session;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class RestartCommand extends Command {
    public RestartCommand(BridgingPractice bridgingPractice) {
        super(bridgingPractice);
    }

    @Override
    public String getName() {
        return "restart";
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

        Session currentSession = bridgingPractice.getActiveSessions().get(player);
        double[] offset = Offsets.getOffsets(currentSession.getSchematicName());
        player.teleport(new Location(Bukkit.getWorld(player.getUniqueId().toString().replaceAll("-", "")), 0.0, 128.0, 0.0).add(offset[0], offset[1] + 1, offset[2]));
        if (bridgingPractice.getActiveSessions().get(player) != null) {
            for (Location loc : currentSession.getBlockPlaced()) {
                player.getWorld().getBlockAt(loc).setType(Material.AIR);
            }
        }
        player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Successfully restarted world.");

    }
}
