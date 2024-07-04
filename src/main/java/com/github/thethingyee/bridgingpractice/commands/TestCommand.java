package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TestCommand extends Command {

    public TestCommand(BridgingPractice bridgingPractice) {
        super(bridgingPractice);
    }

    @Override
    public String getName() {
        return "test";
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
        player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + ChatColor.BOLD + "It works!");
    }
}
