package net.ingoh.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;

import net.minecraft.core.Registry;

public class RandomThings {

    private static RandomThings instance;

    public List<String> keys = new LinkedList<>();

    public RandomThings() {
        /*
        Bukkit.getLogger().info("[IngoBotMC] Loading registries...");
        IRegistry<? extends IRegistry<?>> registries = IRegistry.
        registries.forEach(registry -> {
            Set<?> set = registry.e();
            set.forEach(item -> add(item.toString()));
        });
        
        Bukkit.getLogger().info("[IngoBotMC] Registries loaded!");
        */
    }

    private void add(String str) {
        String[] split = str.split(" / ");
        String registry = split[0].substring(split[0].indexOf("[")+1).replace("minecraft:", "").replace("_", " ").replace(".", " ").replace("/", " ");
        String key = split[1].substring(0, split[1].length()-1).replace("minecraft:", "").replace("_", " ").replace(".", " ").replace("/", " ");
        keys.add(key.toUpperCase() + " " + registry.toLowerCase());
    }

    public static void initialize() {
        //instance = new RandomThings();
    }


    public static String getRandomThing() {
        //if (instance == null) initialize();
        //return instance.keys.get((int)(Math.random() * instance.keys.size())).replace("_", " ");
        return "error";
    }
}
