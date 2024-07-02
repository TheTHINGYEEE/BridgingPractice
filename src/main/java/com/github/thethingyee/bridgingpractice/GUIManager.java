package com.github.thethingyee.bridgingpractice;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class GUIManager {

    private final BridgingPractice bridgingPractice;

    private ItemMeta changeColMeta;
    private ItemMeta restartMeta;
    private ItemMeta refillMeta;
    private ItemMeta leaveMeta;

    private ItemMeta spectatorLeave;

    public GUIManager(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    public void giveInventoryItems(Player player, int color, int changeitemcolor) {

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 1.0F);

        ArrayList<ItemStack> stacks = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        if(!stacks.isEmpty()) {
            player.getInventory().clear();
        }

        ItemStack wool = new ItemStack(Material.WOOL, 64, (byte)color);
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack refill = new ItemStack(Material.CHEST);
        ItemStack changeCol = new ItemStack(Material.INK_SACK, 1, (byte)changeitemcolor);
        ItemStack restart = new ItemStack(Material.STICK);
        ItemStack leave = new ItemStack(Material.REDSTONE_BLOCK);

        refillMeta = refill.getItemMeta();
        refillMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        refillMeta.setDisplayName(ChatColor.YELLOW + "Refill Blocks");

        changeColMeta = changeCol.getItemMeta();
        changeColMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        changeColMeta.setDisplayName(ChatColor.GOLD + "Change Color");

        restartMeta = restart.getItemMeta();
        restartMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        restartMeta.setDisplayName(ChatColor.AQUA + "Restart");

        leaveMeta = leave.getItemMeta();
        leaveMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        leaveMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Leave bridging");

        refill.setItemMeta(refillMeta);
        changeCol.setItemMeta(changeColMeta);
        restart.setItemMeta(restartMeta);
        leave.setItemMeta(leaveMeta);

        ItemStack[] itArr = {wool, wool.clone(), wool.clone(), wool.clone(), air, refill, changeCol, restart, leave};

        player.getInventory().setContents(itArr);
        player.closeInventory();
        player.updateInventory();

    }

    public void giveSpectatorItems(Player player) {
        ItemStack it = new ItemStack(Material.REDSTONE_BLOCK);

        spectatorLeave = it.getItemMeta();
        spectatorLeave.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Leave spectate");
        spectatorLeave.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        it.setItemMeta(spectatorLeave);

        player.getInventory().setItem(8, it);
    }

    public void changeColorGUI(Player player) {

        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.GOLD + "" + ChatColor.BOLD + "Select Color");

        ArrayList<ItemStack> stacks = new ArrayList<>();

        for(int i = 0; i <= 15; i++) {
            stacks.add(new ItemStack(Material.WOOL, 1, (byte)i));
        }

        inv.setContents(stacks.toArray(new ItemStack[0]));

        player.openInventory(inv);

    }

    public ItemMeta getChangeColMeta() {
        return changeColMeta;
    }

    public ItemMeta getRestartMeta() {
        return restartMeta;
    }

    public ItemMeta getRefillMeta() {
        return refillMeta;
    }

    public ItemMeta getLeaveMeta() {
        return leaveMeta;
    }

    public ItemMeta getSpectatorLeave() {
        return spectatorLeave;
    }
}
