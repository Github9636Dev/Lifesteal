package com.hcfpetdev.lifesteal;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RightClickListener implements Listener {

    private ItemStack heartItem;
    private int maxHeartsFromCrafting;

    public RightClickListener(int maxHeartsFromCrafting) {
        heartItem = Main.recipes.get("heart").getItem();
        this.maxHeartsFromCrafting = maxHeartsFromCrafting;
    }
    @EventHandler
    private void onRightClick(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!event.getItem().hasItemMeta()) return;
        if (!event.getItem().isSimilar(heartItem)) return;
        if (event.getPlayer().getMaxHealth() + 2 > maxHeartsFromCrafting * 2) {
            event.getPlayer().sendMessage("§cError: the max amount of hearts from crafting is " + maxHeartsFromCrafting);
            return;
        }

        ItemStack stack = event.getItem();
        if (stack.getAmount()==1) event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
        else {
            stack.setAmount(stack.getAmount() - 1);
            event.getPlayer().setItemInHand(stack);
        }
        event.getPlayer().setMaxHealth(event.getPlayer().getMaxHealth()+2);
        event.getPlayer().setHealth(event.getPlayer().getHealth() + 2);
        event.setCancelled(true);
    }
}
