package net.ingoh.minecraft.plugins.ingobotmc;

import java.util.Arrays;
import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemReward {

    public enum Reward {
        MILESTONE1, MILESTONE2, MILESTONE3
    }

    public static ItemReward from(Reward reward) {
        switch (reward) {
            case MILESTONE1 -> {
                ItemStack stack = new ItemStack(Material.FEATHER);
                ItemMeta itemMeta = stack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.WHITE +"Wiki Contributor Feather");
                itemMeta.setLore(new LinkedList<>(Arrays.asList("Reward for acquiring 50 Simple Wiki Credits")));
                stack.setItemMeta(itemMeta);
                return new ItemReward(stack);
            }
            case MILESTONE2 -> {
                ItemStack stack = new ItemStack(Material.FEATHER);
                ItemMeta itemMeta = stack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.YELLOW + "Super Wiki Contributor Feather");
                itemMeta.setLore(new LinkedList<>(Arrays.asList("Reward for acquiring 300 Simple Wiki Credits")));
                stack.setItemMeta(itemMeta);
                return new ItemReward(stack);
            }
            case MILESTONE3 -> {
                ItemStack stack = new ItemStack(Material.FEATHER);
                ItemMeta itemMeta = stack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.GOLD + "Ultimate Wiki Contributor Feather");
                itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                itemMeta.setLore(new LinkedList<>(Arrays.asList("Reward for acquiring 1500 Simple Wiki Credits")));
                stack.setItemMeta(itemMeta);
                return new ItemReward(stack);
            }
            default -> {
                return null;
            }
        }
    }

    private ItemStack[] reward;

    public ItemReward(ItemStack... items) {
        this.reward = items;
    }

    public ItemStack[] getReward() {
        return reward;
    }

}
