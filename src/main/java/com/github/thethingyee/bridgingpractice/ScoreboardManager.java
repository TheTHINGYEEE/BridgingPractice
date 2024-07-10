package com.github.thethingyee.bridgingpractice;

import com.github.thethingyee.bridgingpractice.utils.Session;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardManager {

    private final BridgingPractice bridgingPractice;

    public ScoreboardManager(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    public void init() {

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : bridgingPractice.getActiveSessions().keySet()) {
                    // spectating player scoreboard

                    if(bridgingPractice.getActiveSessions().get(player).getScoreboard() == null) bridgingPractice.getActiveSessions().get(player).setScoreboard(new FastBoard(player));


                    if(bridgingPractice.getActiveSessions().get(player).isSpectatingPlayer()) {
                        Player target = bridgingPractice.getActiveSessions().get(player).getSpectating();
                        Session session = bridgingPractice.getActiveSessions().get(target);
                        FastBoard fastBoard = bridgingPractice.getActiveSessions().get(player).getScoreboard();
                        fastBoard.updateTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "Bridging");

                        fastBoard.updateLines(
                                "",
                                "Name: " + ChatColor.GREEN + target.getName(),
                                "",
                                "Map: " + ChatColor.GREEN + session.getSchematicName(),
                                "",
                                "Blocks Placed: " + ChatColor.GREEN + session.getBlocksPlaced(),
                                "Speed: " + ChatColor.GREEN + String.format("%.2f", session.getSpeed()) + " m/s",
                                "",
                                "Online Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size(),
                                "",
                                ChatColor.RED + bridgingPractice.getConfig().getString("scoreboard.server-ip")
                        );
                        continue;
                    }

                    // singleplayer session scoreboard
                    Session session = bridgingPractice.getActiveSessions().get(player);
                    FastBoard fastBoard = session.getScoreboard();

                    fastBoard.updateTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "Bridging");

                    fastBoard.updateLines(
                            "",
                            "Name: " + ChatColor.GREEN + player.getName(),
                            "",
                            "Map: " + ChatColor.GREEN + session.getSchematicName(),
                            "",
                            "Blocks Placed: " + ChatColor.GREEN + session.getBlocksPlaced(),
                            "Speed: " + ChatColor.GREEN + String.format("%.2f", session.getSpeed()) + " m/s",
                            "",
                            "Online Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size(),
                            "",
                            ChatColor.RED + bridgingPractice.getConfig().getString("scoreboard.server-ip")
                    );
                }
            }
        };
        runnable.runTaskTimer(bridgingPractice, 0, 5);

//        BukkitRunnable runnable2 = new BukkitRunnable() {
//            @Override
//            public void run() {
//                for(Player p : bridgingPractice.getActiveSessions().keySet()) {
//
//                }
//            }
//        };
//        runnable2.runTaskTimer(bridgingPractice, 0, 5);
    }
}
