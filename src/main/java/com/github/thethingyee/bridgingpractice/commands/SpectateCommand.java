package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpectateCommand extends Command {
    public SpectateCommand(BridgingPractice bridgingPractice) {
        super(bridgingPractice);
    }

    @Override
    public String getName() {
        return "spectate";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public boolean performOnlyAtSession() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length != 1) {
            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "Please specify a player.");
            return;
        }

        if(Bukkit.getPlayer(args[0]) == null) {
            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "That player is not online or doesn't exist.");
            return;
        }

        Player playerTarget = Bukkit.getPlayer(args[0]);

        if(bridgingPractice.getActiveSessions().containsKey(player)) {
            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "You cannot spectate while you're playing.");
            return;
        }

        if (playerTarget == player) {
            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't spectate yourself.");
            return;
        }

        if (!bridgingPractice.getActiveSessions().containsKey(playerTarget)) {
            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "That player is not on any practice worlds.");
            return;
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(true);
        player.setFlying(true);
        playerTarget.hidePlayer(player);
        Location loc = new Location(bridgingPractice.getActiveSessions().get(playerTarget).getAssignedWorld(), 0.0, 128.0, 0.0);
        double[] offset = Offsets.getOffsets(bridgingPractice.getActiveSessions().get(playerTarget).getSchematicName());
        player.teleport(loc.add(offset[0], offset[1]+1, offset[2]));
        bridgingPractice.getGuiManager().giveSpectatorItems(player);
        bridgingPractice.getActiveSessions().get(player).setSpectating(playerTarget);
        player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Now spectating player " + playerTarget.getName() + "!");

    }
}
