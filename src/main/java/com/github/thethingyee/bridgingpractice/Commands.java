package com.github.thethingyee.bridgingpractice;

import com.github.thethingyee.bridgingpractice.utils.HMaps;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class Commands implements CommandExecutor {

    private final BridgingPractice bridgingPractice;

    public Commands(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            HMaps hMaps = bridgingPractice.gethMaps();

            if(cmd.getName().equalsIgnoreCase("bridge")) {
                if(args.length >= 1) {
                    if(args[0].equalsIgnoreCase("reload")) {
                        bridgingPractice.reloadConfig();
                        player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Configuration reloaded...");
                    } else if(args[0].equalsIgnoreCase("practice")) {
                        if (!hMaps.getAssignedWorld().containsKey(player)) {

                            player.sendMessage(bridgingPractice.prefix + ChatColor.YELLOW + "Creating world...");

                            WorldCreator wc = new WorldCreator(player.getUniqueId().toString().replaceAll("-", ""));
                            File file = null;

                            boolean customSchematicFound = false;

                            if(args.length == 2) {
                                String schematicName = args[1];
                                boolean schematicExist = bridgingPractice.getConfigExists().schematicExists(schematicName);
                                String msg = schematicExist ? ChatColor.GREEN + "Schematic file exists!" : ChatColor.RED + "Schematic file doesn't exist.";
                                if(schematicExist) customSchematicFound = true;
                                file = bridgingPractice.getConfigExists().getSchematicFile(schematicName);
                                player.sendMessage(bridgingPractice.prefix + msg);
                            }

                            wc.type(WorldType.FLAT);
                            wc.generatorSettings("2;0;1;");

                            World createdWorld = wc.createWorld();
                            hMaps.getAssignedWorld().put(player, createdWorld);
                            createdWorld.setDifficulty(Difficulty.PEACEFUL);

                            bridgingPractice.getWorldArray().add(player.getUniqueId().toString().replaceAll("-", ""));

                            String schematicName = (args.length == 2) ? args[1] : "default";

                            File prev = file;
                            file = customSchematicFound ? prev : new File(bridgingPractice.getDataFolder() + File.separator + "/schematics/" + bridgingPractice.getConfig().getString("defaults.schematic"));
                            Location loc = new Location(createdWorld, 0.0, 128.0, 0.0);

                            try {
                                bridgingPractice.loadSchem(file, loc, Offsets.getRotation(schematicName));
                            } catch (IOException | DataException | MaxChangedBlocksException e) {
                                e.printStackTrace();
                            }

                            player.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage(bridgingPractice.prefix + ChatColor.YELLOW + "Teleporting to your assigned world...");
                            player.teleport(loc.add(Offsets.getOffsets(schematicName)[0],
                                    Offsets.getOffsets(schematicName)[1],
                                    Offsets.getOffsets(schematicName)[2]));
                            hMaps.getPlayerSchematic().put(player, schematicName);
                            player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Teleported!");

                            bridgingPractice.getGuiManager().giveInventoryItems(player, 0, 15);

                            hMaps.getBlocksPlaced().put(player, 0);

                            PlayerSpeed playerSpeed = bridgingPractice.getPlayerSpeed();

                            playerSpeed.startPlayerSpeed(player);
                            bridgingPractice.getScoreboardManager().showPlayingScoreboard(player);
                            hMaps.getSelectedWoolColor().put(player, (short)0);
                        } else {
                            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "You already have an assigned world.");
                            Location loc = new Location(hMaps.getAssignedWorld().get(player), 0.0, 128.0, 0.0);
                            player.teleport(loc.add(Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[0], Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[1]+1, Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[2]));
                        }
                    }
                    if (player.getWorld().getName().equalsIgnoreCase(player.getUniqueId().toString().replaceAll("-", ""))) {
                        if(args[0].equalsIgnoreCase("changecolor")) {
                            bridgingPractice.getGuiManager().changeColorGUI(player);
                            player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "You have opened the Change Color GUI.");
                        } else if(args[0].equalsIgnoreCase("restart")) {
                            player.teleport(new Location(Bukkit.getWorld(player.getUniqueId().toString().replaceAll("-", "")), 0.0, 128.0, 0.0).add(Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[0], Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[1]+1, Offsets.getOffsets(hMaps.getPlayerSchematic().get(player))[2]));
                            if (hMaps.getBlockPlaced().containsKey(player)) {
                                for (Location loc : hMaps.getBlockPlaced().get(player)) {
                                    player.getWorld().getBlockAt(loc).setType(Material.AIR);
                                }
                            }
                            player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Successfully restarted world.");
                        } else if(args[0].equalsIgnoreCase("leave")) {
                            if(hMaps.getAssignedWorld().containsKey(player)) {
                                player.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                                String worldName = player.getUniqueId().toString().replaceAll("-", "");
                                World world = Bukkit.getWorld(worldName);
                                if(world != null) {
                                    if(!world.getPlayers().isEmpty()) {
                                        for(Player pl : world.getPlayers()) {
                                            if(hMaps.getSpectatingPlayer().get(pl) == player) {
                                                hMaps.getSpectatingPlayer().remove(pl);
                                            }
                                            pl.teleport(Bukkit.getWorld(bridgingPractice.getConfig().getString("defaults.world")).getSpawnLocation());
                                            player.showPlayer(pl);
                                        }
                                    }
                                    Bukkit.unloadWorld(world, false);
                                    try {
                                        FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + "/" + worldName));
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    Bukkit.getConsoleSender().sendMessage("Deleted world '" + worldName + "'");
                                    hMaps.getAssignedWorld().remove(player);
                                    bridgingPractice.getWorldArray().remove(worldName);
                                    hMaps.getPlayerScoreboard().get(player).delete();
                                    hMaps.getPlayerScoreboard().remove(player);

                                    player.sendMessage(bridgingPractice.prefix + ChatColor.YELLOW + "Successfully left bridging...");
                                }
                            }
                            player.getInventory().clear();
                        }
                    }
                }
                if(args.length >= 2) {
                    if(args[0].equalsIgnoreCase("spectate")) {
                        Player playerTarget = Bukkit.getPlayer(args[1]);
                        if(hMaps.getAssignedWorld().containsKey(player)) {
                            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "You cannot spectate while you're playing.");
                            return true;
                        }
                        if(playerTarget != null) {
                            if (playerTarget != player) {
                                if (hMaps.getAssignedWorld().containsKey(playerTarget)) {
                                    player.setGameMode(GameMode.SURVIVAL);
                                    player.setAllowFlight(true);
                                    player.setFlying(true);
                                    playerTarget.hidePlayer(player);
                                    Location loc = new Location(hMaps.getAssignedWorld().get(playerTarget), 0.0, 128.0, 0.0);
                                    player.teleport(loc.add(Offsets.getOffsets(hMaps.getPlayerSchematic().get(playerTarget))[0], Offsets.getOffsets(hMaps.getPlayerSchematic().get(playerTarget))[1]+1, Offsets.getOffsets(hMaps.getPlayerSchematic().get(playerTarget))[2]));
                                    bridgingPractice.getGuiManager().giveSpectatorItems(player);
                                    /*
                                        1. Fix scoreboard interference DONE
                                        2. Fix stuff obviously. // idk what u mean but im doing it now :P
                                        3. Clean up HashMaps when unused. // will do when this plugin is eligible to be fully released (TO THE PUBLIC).
                                    */
                                    bridgingPractice.getScoreboardManager().showSpectateScoreboard(player, playerTarget);
                                    player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Now spectating player " + playerTarget.getName() + "!");
                                } else {
                                    player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "That player is not on any practice worlds.");
                                }
                            } else {
                                player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "You can't spectate yourself.");
                            }
                        } else {
                            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "That player is not online or doesn't exist.");
                        }
                    }
                }
            }
        }
        return true;
    }
}
