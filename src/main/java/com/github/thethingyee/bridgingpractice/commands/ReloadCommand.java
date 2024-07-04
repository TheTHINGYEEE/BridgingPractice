package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class ReloadCommand extends Command {
    public ReloadCommand(BridgingPractice bridgingPractice) {
        super(bridgingPractice);
    }

    @Override
    public String getName() {
        return "reload";
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
        bridgingPractice.reloadConfig();
        player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Configuration reloaded...");

    }
}
