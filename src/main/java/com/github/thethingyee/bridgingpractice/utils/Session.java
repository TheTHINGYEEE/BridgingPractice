package com.github.thethingyee.bridgingpractice.utils;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class Session {

    private World assignedWorld;
    private int blocksPlaced; // how many blocks the player has placed in the session
    private ArrayList<Location> blockPlaced; // the block that was recently placed and its location
    private DyeColor woolColor;
    private double speed;
    private BukkitRunnable runnable;
    private FastBoard scoreboard;
    private String schematicName;
    private Player spectating;

    public World getAssignedWorld() {
        return assignedWorld;
    }

    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    public ArrayList<Location> getBlockPlaced() {
        return blockPlaced;
    }

    public DyeColor getWoolColor() {
        return woolColor;
    }

    public double getSpeed() {
        return speed;
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }

    public FastBoard getScoreboard() {
        return scoreboard;
    }

    public String getSchematicName() {
        return schematicName;
    }

    public Player getSpectating() {
        return spectating;
    }

    public void setAssignedWorld(World assignedWorld) {
        this.assignedWorld = assignedWorld;
    }

    public void setBlocksPlaced(int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    public void setBlockPlaced(ArrayList<Location> blockPlaced) {
        this.blockPlaced = blockPlaced;
    }

    public void setWoolColor(DyeColor woolColor) {
        this.woolColor = woolColor;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setRunnable(BukkitRunnable runnable) {
        this.runnable = runnable;
    }

    public void setScoreboard(FastBoard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setSchematicName(String schematicName) {
        this.schematicName = schematicName;
    }

    public void setSpectating(Player spectating) {
        this.spectating = spectating;
    }

    public boolean isSpectatingPlayer() {
        return spectating != null;
    }
}