package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.Session;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

// i hate you so much old thingy i have to replace all hmap garbage because of your bs i havent even tested the plugin yet if it even works before i did all this
// and now i have to make a schematic bruhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh fuck you
// i have been refactoring for 1 and a half hours fuck
public class PlayerLeave implements Listener {

    private final BridgingPractice bridgingPractice;

    public PlayerLeave(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        if(!bridgingPractice.getActiveSessions().containsKey(e.getPlayer())) return;

        Session currentSession = bridgingPractice.getActiveSessions().get(e.getPlayer());

        p.getInventory().clear();

        if(currentSession.getAssignedWorld() != null) {
            currentSession.leaveSession(bridgingPractice, p);
        }

    }
}
