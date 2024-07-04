package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class ChangeColorCommand extends Command {

    public ChangeColorCommand(BridgingPractice bridgingPractice) {
        super(bridgingPractice);
    }

    @Override
    public String getName() {
        return "changecolor";
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
        bridgingPractice.getGuiManager().changeColorGUI(player);
        player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "You have opened the Change Color GUI.");
    }
}
