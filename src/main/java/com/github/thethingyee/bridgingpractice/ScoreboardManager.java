package com.github.thethingyee.bridgingpractice;

import com.github.thethingyee.bridgingpractice.libraries.FastBoard;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
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
        HMaps hMaps = bridgingPractice.gethMaps();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : hMaps.getPlayerScoreboard().keySet()) {
                    if(hMaps.getSpectatingPlayer().containsKey(player)) {
                        continue;
                    }
                    FastBoard fastBoard = hMaps.getPlayerScoreboard().get(player);
                    fastBoard.updateTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "Bridging");

                    fastBoard.updateLines(
                            "",
                            "Name: " + ChatColor.GREEN + player.getName(),
                            "",
                            "Blocks Placed: " + ChatColor.GREEN + hMaps.getBlocksPlaced().get(player),
                            "Speed: " + ChatColor.GREEN + String.format("%.2f", hMaps.getPlayerSpeed().get(player)) + " m/s",
                            "",
                            "Online Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size(),
                            "",
                            ChatColor.RED + bridgingPractice.getConfig().getString("scoreboard.server-ip")
                    );
                }
            }
        };
        runnable.runTaskTimer(bridgingPractice, 0, 5);

        BukkitRunnable runnable2 = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : hMaps.getSpectatingPlayer().keySet()) {
                    if(hMaps.getSpectatingPlayer().get(p) != null) {
                        Player target = hMaps.getSpectatingPlayer().get(p);
                        FastBoard fastBoard = hMaps.getPlayerScoreboard().get(p);
                        fastBoard.updateTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "Bridging");

                        fastBoard.updateLines(
                                "",
                                "Name: " + ChatColor.GREEN + target.getName(),
                                "",
                                "Blocks Placed: " + ChatColor.GREEN + hMaps.getBlocksPlaced().get(target),
                                "Speed: " + ChatColor.GREEN + String.format("%.2f", hMaps.getPlayerSpeed().get(target)) + " m/s",
                                "",
                                "Online Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size(),
                                "",
                                ChatColor.RED + bridgingPractice.getConfig().getString("scoreboard.server-ip")
                        );
                    }
                }
            }
        };
        runnable2.runTaskTimer(bridgingPractice, 0, 5);
    }

    public void showPlayingScoreboard(Player player) {
        HMaps hMaps = bridgingPractice.gethMaps();
        hMaps.getPlayerScoreboard().put(player, new FastBoard(player));
    }

    public void showSpectateScoreboard(Player request, Player target) {
        HMaps hMaps = bridgingPractice.gethMaps();
        hMaps.getPlayerScoreboard().put(request, new FastBoard(request));
        hMaps.getSpectatingPlayer().put(request, target);
    }
}
