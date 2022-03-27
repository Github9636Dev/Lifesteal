package com.hcfpetdev.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;


public class LifeStealListener implements Listener {

    private String whenDead;
    public static ItemStack deadPlayer;
    private JavaPlugin instance;
    private int maxHealth;

    public LifeStealListener(JavaPlugin instance) {
        whenDead = instance.getConfig().getString("whenDead").toLowerCase(Locale.ROOT);

        deadPlayer = new ItemStack(Material.BEDROCK);
        deadPlayer.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.BEDROCK);
        meta.setDisplayName("§7DEAD");
        deadPlayer.setItemMeta(meta);


        maxHealth = instance.getConfig().getInt("maxhearts");
        this.instance = instance;
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {

        if (event.getEntity().getMaxHealth() <= 2) {
            Bukkit.broadcastMessage("§c" + event.getEntity().getName() + " §ehas 0 hearts");
            Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> {
                event.getEntity().getInventory().addItem(deadPlayer);
                event.getEntity().kickPlayer("Please Relog");
            },5);

        }
        else event.getEntity().setMaxHealth(event.getEntity().getMaxHealth()-2);

        if (event.getEntity().getKiller() != null) {
            Player p = event.getEntity().getKiller();
            if (p.getMaxHealth() <= maxHealth -2) p.setMaxHealth(p.getMaxHealth() + 2);
            else p.sendMessage("§cError: you are the max amount of hearts " + maxHealth);
        }


    }
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().getInventory().contains(deadPlayer)) return;
        if (whenDead.equals("spectate")) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            System.out.println("Player gamemode set to spectator");
        }
        else if (whenDead.equals("kick")) event.getPlayer().kickPlayer("You are dead on this server");
        event.setJoinMessage("§e" + event.getPlayer().getDisplayName() + " has joined the server");

    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(deadPlayer)) event.setCancelled(true);
    }
}
