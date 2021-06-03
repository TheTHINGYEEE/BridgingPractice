package com.github.thethingyee.bridgingpractice.utils;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import org.bukkit.Bukkit;

import java.io.File;

public class ConfigExists {

    private final BridgingPractice bridgingPractice;

    public ConfigExists(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    public boolean schematicExists(String schematicName) {
        if(bridgingPractice.getConfig().get("offset." + schematicName) != null) {
            File file = new File(bridgingPractice.getDataFolder() + File.separator + "/schematics/" + schematicName + ".schematic");
            return (file.exists());
        }
        return false;
    }

    public File getSchematicFile(String schematicName) {
        if(schematicExists(schematicName)) {
            return (new File(bridgingPractice.getDataFolder() + File.separator + "/schematics/" + schematicName + ".schematic"));
        }
        return null;
    }
}
