package com.hcfpetdev.lifesteal;

import com.hcfpetdev.lifesteal.commands.Revive;
import com.hcfpetdev.lifesteal.commands.WithdrawHearts;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    private String name, version;
    public static HashMap<String, CustomRecipe> recipes;
    public static HashMap<ItemStack, String > tags;

    @Override
    public void onEnable() {
        super.onEnable();

        FileConfiguration config = getConfig();

        name = getDescription().getName();
        version = getDescription().getVersion();

        if (!new File(config.getCurrentPath()).exists()) saveDefaultConfig();

        if (!config.getString("reviveCommand").equals("")) new Revive(this);

        recipes = new HashMap<>();
        tags = new HashMap<>();

        String path, name, tag;
        List<String> lore,temp;
        ItemStack item;
        ItemMeta meta;
        CustomRecipe recipe;

        for (int i = 0; config.get("crafting.recipe" + i) != null; i++) {
            path = "crafting.recipe" + i + ".";
            name = config.getString(path + "name");
            lore = config.getStringList(path + "lore");
            item = new ItemStack(Material.getMaterial(config.getString(path + "item")));
            tag = config.getString(path + "tag");

            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            if (lore.size() > 0) meta.setLore(lore);
            meta.setDisplayName("§r" + name);
            item.setItemMeta(meta);


            item.addUnsafeEnchantment(Enchantment.DURABILITY,1);

            recipe = new CustomRecipe(item);

            if (config.getBoolean(path + "shapeless")) {
                ShapelessRecipe shapelessRecipe = new ShapelessRecipe(item);
                for (int j = 0; config.get(path + "material" + j) != null; j++) {
                    Material m = Material.getMaterial(config.getString(path + "material" + j));
                    if (m == null) {
                        m = recipes.get(config.getString(path + "material" + j)).getItem().getType();
                        recipe.addCustom(config.getString(path + "material" + j));
                    }
                    if (m == Material.AIR || m == null) System.out.println("Crafting item error on recipe " + tag);

                    System.out.println(m.name());

                    shapelessRecipe.addIngredient(m);
                }
                recipe.addRecipe(shapelessRecipe);
            }

            else {
                ShapedRecipe shapedRecipe = new ShapedRecipe(item);

                System.out.println(config.getString(path + "line1"));

                shapedRecipe.shape(config.getString(path + "line1"), config.getString(path + "line2"),
                        config.getString(path + "line3"));

                for (int j = 0; config.get(path + "ingredient" + j) != null; j++) {
                    temp = config.getStringList(path + "ingredient" + j);
                    Material m = Material.getMaterial(temp.get(1));
                    System.out.println("Temp: " + temp + "\nMaterial: "+m );
                    if (m == null) {
                        if (recipes.containsKey(temp.get(1))) {
                            m = recipes.get(temp.get(1)).getItem().getType();
                            recipe.addCustom(temp.get(1));
                        }
                        else System.out.println("Error on string:" + temp.get(1));
                    }
                    shapedRecipe.setIngredient(temp.get(0).charAt(0), m);
                }

                recipe.addRecipe(shapedRecipe);
            }

            System.out.println("Tag: " + tag + "   Recipe: " + recipe.getItem().getType().name());
            recipes.put(tag, recipe);
            tags.put(recipe.getItem(), tag);

            Bukkit.addRecipe(recipe.getRecipe());

            System.out.println(recipe.toString());

        }

        int maxHeartsFromCraft = config.getInt("maxheartsfromcrafting");
        boolean usableHearts = config.getBoolean("craftableHearts");
        String alias = config.getString("withdrawHearts");

        new WithdrawHearts(this,alias);

        Bukkit.getPluginManager().registerEvents(new LifeStealListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CraftingListener(this),this);
        Bukkit.getPluginManager().registerEvents(new RightClickListener(maxHeartsFromCraft, usableHearts),this);
    }

}
