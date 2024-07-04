package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import com.github.thethingyee.bridgingpractice.commands.manager.CommandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand extends Command {
    public HelpCommand(BridgingPractice bridgingPractice) {
        super(bridgingPractice);
    }

    @Override
    public String getName() {
        return "help";
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
        StringBuilder builder = new StringBuilder();
        builder.append("---" + ChatColor.GOLD + ChatColor.BOLD + "Bridging Practice Help" + ChatColor.RESET + "---\n");
        for(int i = 0; i < CommandManager.registeredSubCommands.size(); i++) {
            Command cmd = CommandManager.registeredSubCommands.get(i);
            builder.append(i + 1).append(". /bridge ").append(cmd.getName()).append(" ").append(cmd.getArguments()).append("\n");
        }
        builder.append("-----------------------------");
        player.sendMessage(builder.toString());
    }
}
