package com.hcfpetdev.lifesteal;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipe {

    private ItemStack item;
    private Recipe recipe;
    private List<String> customItems;

    public CustomRecipe(ItemStack item) {
        this.item = item;
        customItems = new ArrayList<>();
    }

    public List<String> getCustomItems() {
        return customItems;
    }

    public void addRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public ItemStack getItem() {
        return item;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "CustomRecipe{" +
                "item=" + item +
                ", recipe=" + recipe +
                '}';
    }

    public void addCustom(String customName) {
        customItems.add(customName);
    }
}
