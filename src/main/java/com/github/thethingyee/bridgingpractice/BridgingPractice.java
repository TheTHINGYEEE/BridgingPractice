package com.github.thethingyee.bridgingpractice;

import com.github.thethingyee.bridgingpractice.listeners.*;
import com.github.thethingyee.bridgingpractice.utils.ConfigExists;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public final class BridgingPractice extends JavaPlugin implements Listener {

    private final ArrayList<String> worldArray = new ArrayList<>();
    public String prefix = ChatColor.translateAlternateColorCodes('&', "&7[&6&lBridgingPractice&7] &f");

    private GUIManager guiManager;
    private ScoreboardManager scoreboardManager;

    private PlayerSpeed playerSpeed;
    private HMaps hMaps;
    private ConfigExists configExists;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + "Enabling BridgingPractice plugin..");

        guiManager = new GUIManager(this);
        scoreboardManager = new ScoreboardManager(this);

        new Offsets(this);

        playerSpeed = new PlayerSpeed(this);
        hMaps = new HMaps();
        configExists = new ConfigExists(this);

        if (getWorldEditPlugin() == null) {
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "WorldEdit not found. Disabling..");

            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
        this.getServer().getPluginManager().registerEvents(new ChangeWorld(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamage(this), this);
        this.getServer().getPluginManager().registerEvents(new InvClick(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeave(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMove(this), this);

        this.getCommand("bridge").setExecutor(new Commands(this));

        saveDefaultConfig();
        if (Bukkit.getWorld(this.getConfig().getString("defaults.world")) == null) {
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "The default world doesn't exist. Disabling...");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        this.getScoreboardManager().init();

        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully enabled BridgingPractice plugin!");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Plugin disabling. Kicking all players.");
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.teleport(Bukkit.getWorld(this.getConfig().getString("defaults.world")).getSpawnLocation());
            player.kickPlayer(prefix + ChatColor.RED + "Plugin disabled. Sorry for the inconvenience.");
        }
        for (String s : getWorldArray()) {
            if (!getWorldArray().isEmpty()) {
                try {
                    File f = new File(Bukkit.getWorldContainer() + File.separator + "/" + s);
                    if(f.exists()) {
                        Bukkit.unloadWorld(Bukkit.getWorld(s), false);
                        FileUtils.deleteDirectory(f);
                    }
                    getWorldArray().remove(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadSchem(File file, Location loc, int rotation) throws IOException, MaxChangedBlocksException, DataException {
        EditSession session = this.getWorldEditPlugin().getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), 1000);
        CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
        clipboard.rotate2D(rotation);
        clipboard.paste(session, new Vector(loc.getX(), loc.getY(), loc.getZ()), false);
    }

    public PlayerSpeed getPlayerSpeed() {
        return playerSpeed;
    }

    public HMaps gethMaps() {
        return hMaps;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ArrayList<String> getWorldArray() {
        return worldArray;
    }

    public ConfigExists getConfigExists() {
        return configExists;
    }

    private WorldEditPlugin getWorldEditPlugin() {
        return (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    }
}
