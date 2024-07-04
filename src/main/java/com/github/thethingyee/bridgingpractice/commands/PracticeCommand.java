package com.github.thethingyee.bridgingpractice.commands;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.PlayerSpeed;
import com.github.thethingyee.bridgingpractice.commands.manager.Command;
import com.github.thethingyee.bridgingpractice.utils.Offsets;
import com.github.thethingyee.bridgingpractice.utils.Session;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PracticeCommand extends Command {

    public PracticeCommand(BridgingPractice bridgingPractice) {
        super(bridgingPractice);
    }

    @Override
    public String getName() {
        return "practice";
    }

    @Override
    public String getArguments() {
        return "<schematic_name>";
    }

    @Override
    public boolean performOnlyAtSession() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (!bridgingPractice.getActiveSessions().containsKey(player)) {

            player.sendMessage(bridgingPractice.prefix + ChatColor.YELLOW + "Creating world...");

            WorldCreator wc = new WorldCreator(player.getUniqueId().toString().replaceAll("-", ""));
            File file = null;

            boolean customSchematicFound = false;

            if(args.length == 1) {
                String schematicName = args[0];
                boolean schematicExist = bridgingPractice.getConfigExists().schematicExists(schematicName, player);
                String msg = schematicExist ? ChatColor.GREEN + "Schematic file exists!" : ChatColor.RED + "Schematic file doesn't exist.";

                if(schematicExist) customSchematicFound = true;
                file = bridgingPractice.getConfigExists().getSchematicFile(schematicName, player);
                player.sendMessage(bridgingPractice.prefix + msg);
            }

            if(file == null) return;

            wc.type(WorldType.FLAT);
            wc.generatorSettings("2;0;1;");

            World createdWorld = wc.createWorld();
            bridgingPractice.getActiveSessions().put(player, new Session());

            Session currentSession = bridgingPractice.getActiveSessions().get(player);
            bridgingPractice.getScoreboardManager().showPlayingScoreboard(player);

            currentSession.setAssignedWorld(createdWorld);
//                            hMaps.getAssignedWorld().put(player, createdWorld);
            createdWorld.setDifficulty(Difficulty.PEACEFUL);

            bridgingPractice.getWorldArray().add(player.getUniqueId().toString().replaceAll("-", ""));

            String schematicName = (args.length == 1) ? args[0] : file.getName().replaceAll(".schematic", "");

            File prev = file;
            file = customSchematicFound ? prev : new File(bridgingPractice.getDataFolder() + File.separator + "/schematics/" + bridgingPractice.getConfig().getString("defaults.schematic"));
            Location loc = new Location(createdWorld, 0.0, 128.0, 0.0);

            try {
                bridgingPractice.loadSchem(file, loc, Offsets.getRotation(schematicName));
            } catch (IOException | DataException | MaxChangedBlocksException e) {
                Bukkit.getConsoleSender().sendMessage("Exception found! " + e.getMessage());
            }

            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(bridgingPractice.prefix + ChatColor.YELLOW + "Teleporting to your assigned world...");
            player.teleport(loc.add(Offsets.getOffsets(schematicName)[0],
                    Offsets.getOffsets(schematicName)[1],
                    Offsets.getOffsets(schematicName)[2]));
            currentSession.setSchematicName(schematicName);
            player.sendMessage(bridgingPractice.prefix + ChatColor.GREEN + "Teleported!");

            bridgingPractice.getGuiManager().giveInventoryItems(player, DyeColor.WHITE);

            currentSession.setBlocksPlaced(0);

            PlayerSpeed playerSpeed = bridgingPractice.getPlayerSpeed();

            playerSpeed.startPlayerSpeed(player);
            currentSession.setWoolColor(DyeColor.WHITE);
        } else {
            player.sendMessage(bridgingPractice.prefix + ChatColor.RED + "You already have an assigned world.");
            Location loc = new Location(bridgingPractice.getActiveSessions().get(player).getAssignedWorld(), 0.0, 128.0, 0.0);
            player.teleport(loc.add(Offsets.getOffsets(bridgingPractice.getActiveSessions().get(player).getSchematicName())[0], Offsets.getOffsets(bridgingPractice.getActiveSessions().get(player).getSchematicName())[1] + 1, Offsets.getOffsets(bridgingPractice.getActiveSessions().get(player).getSchematicName())[2]));
        }
    }
}
