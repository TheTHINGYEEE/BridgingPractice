package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.PlayerSpeed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangeWorld implements Listener {

    private final BridgingPractice bridgingPractice;

    public ChangeWorld(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        PlayerSpeed playerSpeed = bridgingPractice.getPlayerSpeed();
        if(e.getFrom().getName().equalsIgnoreCase(e.getPlayer().getUniqueId().toString().replaceAll("-", ""))) {
            playerSpeed.stopPlayerSpeed(e.getPlayer());
            bridgingPractice.getActiveSessions().get(e.getPlayer()).setSchematicName(null);
            bridgingPractice.getActiveSessions().get(e.getPlayer()).getScoreboard().delete();
            bridgingPractice.getActiveSessions().get(e.getPlayer()).setScoreboard(null);
        }
    }
}
