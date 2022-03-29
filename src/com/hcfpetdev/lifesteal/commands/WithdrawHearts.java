package com.hcfpetdev.lifesteal.commands;

import com.hcfpetdev.lifesteal.CommandHandler;
import com.hcfpetdev.lifesteal.LifeStealListener;
import com.hcfpetdev.lifesteal.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WithdrawHearts {
    public WithdrawHearts(JavaPlugin instance, String alias) {
        ItemStack heartItem = Main.recipes.get("heart").getItem();

        new CommandHandler(instance, alias, true) {

            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player player = (Player)sender;
                if (player.getMaxHealth() >= 4) {
                    player.setMaxHealth(player.getMaxHealth()-2);
                    player.getInventory().addItem(heartItem);
                }
                else player.sendMessage("§cError: you are too low to use this command");
                return true;
            }

            @Override
            public String getUsage() {
                return "§c/" + alias;
            }

            @Override
            public String getDescription() {
                return "withdraws a heart as an item";
            }
        };
    }
}
