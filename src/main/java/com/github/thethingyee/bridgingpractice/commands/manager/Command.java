package com.github.thethingyee.bridgingpractice.commands.manager;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import org.bukkit.entity.Player;

public abstract class Command {

    public final BridgingPractice bridgingPractice;

    public Command(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    public abstract String getName();
    public abstract String getArguments();
    public abstract boolean performOnlyAtSession();
    public abstract void perform(Player player, String[] args);

}
