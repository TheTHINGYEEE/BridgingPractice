package com.github.thethingyee.bridgingpractice;

import com.github.thethingyee.bridgingpractice.utils.Session;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSpeed {

    private final BridgingPractice bridgingPractice;

    public PlayerSpeed(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    public void startPlayerSpeed(Player player) {

        if(!bridgingPractice.getActiveSessions().containsKey(player)) return;
        Session currentSession = bridgingPractice.getActiveSessions().get(player);

        BukkitRunnable runnable = new BukkitRunnable() {
            Location oldLoc = player.getLocation();
            @Override
            public void run() {
                oldLoc.setY(0.0);
                Location newLoc = player.getLocation();
                newLoc.setY(0.0);
                double equation = newLoc.distance(oldLoc) * 2;
                currentSession.setSpeed(equation);
                oldLoc = newLoc;
            }
        };
        if(currentSession.getRunnable() != null) return;
        currentSession.setRunnable(runnable);
        currentSession.getRunnable().runTaskTimer(bridgingPractice, 0, 10);
    }

    public void stopPlayerSpeed(Player player) {

        if(bridgingPractice.getActiveSessions().get(player).getRunnable() == null) return;
        bridgingPractice.getActiveSessions().get(player).getRunnable().cancel();
        bridgingPractice.getActiveSessions().get(player).setRunnable(null);
    }
}
