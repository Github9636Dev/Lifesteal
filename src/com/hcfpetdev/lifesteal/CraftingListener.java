package com.hcfpetdev.lifesteal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CraftingListener implements Listener {

    private JavaPlugin instance;

    public CraftingListener(JavaPlugin instance) {
        this.instance = instance;
    }

    @EventHandler
    private void onCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe().getResult().getItemMeta() == null) return;
        if (!Main.tags.containsKey(event.getRecipe().getResult())) return;

        String tag = Main.tags.get(event.getRecipe().getResult());

        CustomRecipe recipe = Main.recipes.get(tag);

        List<String> customItems = recipe.getCustomItems();
        List<ItemStack> items = new ArrayList<>();

        for (String s : customItems) items.add(Main.recipes.get(s).getItem());

        for (ItemStack itemStack : event.getInventory().getContents()) items.remove(itemStack);

        if (!items.isEmpty()) {
            Player player = (Player)event.getView().getPlayer();
            player.closeInventory();
            player.sendMessage("§cError: Please use the proper materials to craft this item");
        }


    }
}
