package com.github.thethingyee.bridgingpractice.utils;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

public class ConfigExists {

    private final BridgingPractice bridgingPractice;

    public ConfigExists(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    public boolean schematicExists(String schematicName, Player player) {
        if(bridgingPractice.getConfig().get("offset." + schematicName) != null) {
            File file = new File(bridgingPractice.getDataFolder() + File.separator + "schematics" + File.separator + schematicName + ".schematic");
            player.sendMessage(file.getAbsolutePath());
            return (file.exists());
        }
        return false;
    }

    public File getSchematicFile(String schematicName, Player player) {
        if(schematicExists(schematicName, player)) {
            return (new File(bridgingPractice.getDataFolder() + File.separator + "/schematics/" + schematicName + ".schematic"));
        }
        return null;
    }
}
