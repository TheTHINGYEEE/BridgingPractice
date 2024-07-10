package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
        if (!bridgingPractice.getActiveSessions().containsKey(player)) return;
        player.sendMessage(ChatColor.RED + "Leaving current session...");
        if (!bridgingPractice.getActiveSessions().get(player).leaveSession(bridgingPractice, player)) {
            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "Can't leave session.");
            return;
        }
        player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Successfully left session...");
    }
}
