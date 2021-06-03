package com.github.thethingyee.bridgingpractice.utils;

import com.github.thethingyee.bridgingpractice.libraries.FastBoard;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class HMaps {

    private final HashMap<Player, World> assignedWorld = new HashMap<>();
    private final HashMap<Player, ArrayList<Location>> blockPlaced = new HashMap<>();
    private final HashMap<Player, Integer> blocksPlaced = new HashMap<>();
    private final HashMap<Player, Short> selectedWoolColor = new HashMap<>();
    private final HashMap<Player, Double> playerSpeed = new HashMap<>();
    private final HashMap<Player, BukkitRunnable> playerRunnable = new HashMap<>();
    private final HashMap<Player, FastBoard> playerScoreboard = new HashMap<>();
    private final HashMap<Player, String> playerSchematic = new HashMap<>();
    private final HashMap<Player, Player> spectatingPlayer = new HashMap<>();

    public HashMap<Player, World> getAssignedWorld() {
        return assignedWorld;
    }

    public HashMap<Player, ArrayList<Location>> getBlockPlaced() {
        return blockPlaced;
    }

    public HashMap<Player, Integer> getBlocksPlaced() {
        return blocksPlaced;
    }

    public HashMap<Player, Short> getSelectedWoolColor() {
        return selectedWoolColor;
    }

    public HashMap<Player, Double> getPlayerSpeed() {
        return playerSpeed;
    }

    public HashMap<Player, BukkitRunnable> getPlayerRunnable() {
        return playerRunnable;
    }

    public HashMap<Player, FastBoard> getPlayerScoreboard() {
        return playerScoreboard;
    }

    public HashMap<Player, String> getPlayerSchematic() {
        return playerSchematic;
    }

    public HashMap<Player, Player> getSpectatingPlayer() {
        return spectatingPlayer;
    }
}
