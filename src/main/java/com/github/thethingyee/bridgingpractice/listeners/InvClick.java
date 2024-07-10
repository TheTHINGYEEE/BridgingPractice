package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

public class InvClick implements Listener {

    private final BridgingPractice bridgingPractice;

    public InvClick(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if(e.getInventory() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if (!e.getInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Select Color")) return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        MaterialData matData = e.getCurrentItem().getData();

        if(!(matData instanceof Wool)) return;

        bridgingPractice.getGuiManager().giveInventoryItems(player, ((Wool) matData).getColor());
        bridgingPractice.getActiveSessions().get(player).setWoolColor(((Wool) matData).getColor());
        player.closeInventory();
    }
}
