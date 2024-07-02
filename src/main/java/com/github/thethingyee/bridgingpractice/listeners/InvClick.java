package com.github.thethingyee.bridgingpractice.listeners;

import com.github.thethingyee.bridgingpractice.BridgingPractice;
import com.github.thethingyee.bridgingpractice.utils.HMaps;
import com.github.thethingyee.bridgingpractice.utils.WoolColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClick implements Listener {

    private final BridgingPractice bridgingPractice;

    public InvClick(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        HMaps hMaps = bridgingPractice.gethMaps();
        if(e.getInventory() == null) return;
        if(e.getCurrentItem().getItemMeta() != null) {
            if (e.getInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Select Color")) {
                e.setCancelled(true);
                if (e.getWhoClicked() instanceof Player) {
                    Player player = (Player) e.getWhoClicked();
                    bridgingPractice.getGuiManager().giveInventoryItems(player, e.getCurrentItem().getDurability(), WoolColor.woolToDye(e.getCurrentItem().getDurability()));
                    hMaps.getSelectedWoolColor().put(player, e.getCurrentItem().getDurability());
                    player.closeInventory();
                }
            }
        }
    }
}
