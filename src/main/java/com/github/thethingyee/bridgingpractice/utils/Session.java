package com.github.thethingyee.bridgingpractice.utils;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import fr.mrmicky.fastboard.FastBoard;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    public boolean leaveSession(BridgingPractice bridgingPractice, Player player) {
        player.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
        String worldName = this.getAssignedWorld().getName();
        World world = this.getAssignedWorld();
        if (world != null) {
            if(!world.getPlayers().isEmpty()) {
                for(Player p : world.getPlayers()) {
                    if(bridgingPractice.getActiveSessions().get(p).getSpectating() == player) {
                        this.getScoreboard().delete();
                        this.setScoreboard(null);
                        this.setSpectating(null);
                        p.setAllowFlight(false);
                        p.setFlying(false);
                        p.getInventory().clear();
                    }
                    p.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                    player.showPlayer(player);
                }
            }
            Bukkit.unloadWorld(world, false);
            try {
                FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + "/" + worldName));
            } catch (IOException ex) {
                Bukkit.getConsoleSender().sendMessage(bridgingPractice.prefix + ChatColor.RED + ex.getMessage());
                return false;
            }
            Bukkit.getConsoleSender().sendMessage(bridgingPractice.prefix + "Deleted world '" + worldName + "'");
            this.setAssignedWorld(null);
            this.setSchematicName(null);
            bridgingPractice.getActiveSessions().remove(player);
            bridgingPractice.getWorldArray().remove(worldName);
            this.blockPlaced = null;
            this.blocksPlaced = 0;
            this.woolColor = null;
            this.runnable = null;
            this.speed = 0.0d;
            return true;
        }
        return false;
    }
}
