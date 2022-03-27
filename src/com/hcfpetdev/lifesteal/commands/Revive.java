package com.hcfpetdev.lifesteal.commands;

import com.hcfpetdev.lifesteal.CommandHandler;
import com.hcfpetdev.lifesteal.LifeStealListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Revive {
    public Revive(JavaPlugin instance) {

        String command = instance.getConfig().getString("reviveCommand");
        int maxHealth = instance.getConfig().getInt("reviveHearts") * 2;
        boolean broadcast = instance.getConfig().getBoolean("broadcastRevive");

        new CommandHandler(instance, command, 0,1, false) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                if (!sender.isOp()) {
                    sender.sendMessage("§cYou need to be an operator to use this command");
                    return true;
                }

                if (args.length < 1 && !(sender instanceof Player)) return false;

                Player player;

                if (args.length > 0) player = Bukkit.getPlayer(args[0]);
                else player = (Player) sender;

                if (player == null) return false;


                if (player.getInventory().contains(LifeStealListener.deadPlayer)) {
                    player.setMaxHealth(maxHealth);
                    player.getInventory().clear();
                    player.setGameMode(GameMode.SURVIVAL);
                    if (broadcast) Bukkit.broadcastMessage("§e" + player.getName() + " has been revived by §b" + sender.getName());
                    sender.sendMessage("§eYou have revived " + player.getName());
                    if (!sender.getName().equals(player.getName())) player.sendMessage(
                            "§eYou have been revived by §b" + sender.getName());
                }
                else sender.sendMessage("§cError: that player is not dead");


                return true;
            }

            @Override
            public String getUsage() {
                return "§c/revive <player>";
            }

            @Override
            public String getDescription() {
                return "";
            }
        };
    }
}
