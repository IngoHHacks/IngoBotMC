package tv.ingoh.util;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Registry;

import net.md_5.bungee.chat.TranslationRegistry;

public class RandomThings {

    private static RandomThings instance;

    public List<String> keys = new LinkedList<>();

    public RandomThings() {
        Bukkit.getLogger().info("[IngoBotCore] Loading registries...");
        Registry.MATERIAL.forEach(x -> add("item.minecraft." + x.getKey().getKey(), "item"));
        Registry.SOUNDS.forEach(x -> add("subtitles." + x.getKey().getKey(), "sound"));
        Registry.ENTITY_TYPE.forEach(x -> add("entity.minecraft." + x.getKey().getKey(), "entity"));
        Registry.ATTRIBUTE.forEach(x -> add("attribute.name." + x.getKey().getKey(), "attribute"));
        Registry.BIOME.forEach(x -> add("biome.minecraft." + x.getKey().getKey(), "biome"));
        Registry.STATISTIC.forEach(x -> add("stat.minecraft." + x.getKey().getKey(), "statistic"));
        Registry.ENCHANTMENT.forEach(x -> add("enchantment.minecraft." + x.getKey().getKey(), "enchantment"));
        Bukkit.getLogger().info("[IngoBotCore] Registries loaded!");
    }

    private void add(String str, String str2) {
        String tr = TranslationRegistry.INSTANCE.translate(str);
        if (!tr.equals(str)) {
            keys.add(tr + " " + str2);
        }
    }

    public static void initialize() {
        instance = new RandomThings();
    }

    public static String getRandomThing() {
        if (instance == null) initialize();
        return instance.keys.get((int)(Math.random() * instance.keys.size())).replace("_", " ");
    }
}
