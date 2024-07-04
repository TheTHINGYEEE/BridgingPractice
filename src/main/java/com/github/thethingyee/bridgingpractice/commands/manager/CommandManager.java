package com.github.thethingyee.bridgingpractice.commands.manager;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager implements CommandExecutor {

    public static ArrayList<com.github.thethingyee.bridgingpractice.commands.manager.Command> registeredSubCommands = new ArrayList<>();

    private final BridgingPractice bridgingPractice;

    public CommandManager(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;

        registeredSubCommands.add(new TestCommand(bridgingPractice));
        registeredSubCommands.add(new SpectateCommand(bridgingPractice));
        registeredSubCommands.add(new RestartCommand(bridgingPractice));
        registeredSubCommands.add(new ReloadCommand(bridgingPractice));
        registeredSubCommands.add(new PracticeCommand(bridgingPractice));
        registeredSubCommands.add(new LeaveCommand(bridgingPractice));
        registeredSubCommands.add(new HelpCommand(bridgingPractice));
        registeredSubCommands.add(new ChangeColorCommand(bridgingPractice));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("bridge")) {
                if(!(args.length >= 1)) {
                    new HelpCommand(bridgingPractice).perform(player, args);
                    return true;
                }
                for(com.github.thethingyee.bridgingpractice.commands.manager.Command registeredCommand : registeredSubCommands) {
                    if(registeredCommand.getName().equalsIgnoreCase(args[0])) {
                        if(registeredCommand.performOnlyAtSession()) {
                            if (player.getWorld().getName().equalsIgnoreCase(player.getUniqueId().toString().replaceAll("-", "")))
                                registeredCommand.perform(player, Arrays.copyOfRange(args, 1, args.length));
                            continue;
                        }
                        registeredCommand.perform(player, Arrays.copyOfRange(args, 1, args.length));
                    }
                }
            }
        }
        return true;
    }
}
