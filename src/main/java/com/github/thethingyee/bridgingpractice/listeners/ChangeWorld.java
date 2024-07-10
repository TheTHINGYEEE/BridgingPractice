package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
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
        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;
        if(e.getPlayer().getWorld().equals(bridgingPractice.getActiveSessions().get(e.getPlayer()).getAssignedWorld())) {
            bridgingPractice.getActiveSessions().get(e.getPlayer()).setSchematicName(null);
            if(bridgingPractice.getActiveSessions().get(e.getPlayer()).getScoreboard() != null) {
                bridgingPractice.getActiveSessions().get(e.getPlayer()).getScoreboard().delete();
                bridgingPractice.getActiveSessions().get(e.getPlayer()).setScoreboard(null);
            }
        }
    }
}
