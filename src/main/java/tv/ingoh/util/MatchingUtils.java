package tv.ingoh.util;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;

public class MatchingUtils {

    public static <E extends Enum<E>> String matchClosest(Class<E> cls, String match) {
        try {
            List<String> closest = new LinkedList<>();
            double max = 0.35;
            for (E en : EnumSet.allOf(cls)) {
                double d = difference(en.name(), match) ;
                if (d >= max) {
                    if (d > max) {
                        max = d;
                        closest = new LinkedList<>();
                    }
                    closest.add(en.name());
                }
            }
            String out = "";
            for (int i = 0; i < Math.min(3, closest.size()); i++) {
                if (!out.equals("")) out += ", ";
                out += closest.get(i);
            }
            return out.equals("") ? null : out;
        } catch (Exception e) {
            return null;
        }
    }

    public static double difference(String s1, String s2) {
        if (s1.length() < s2.length()) {
            String temp = s2;
            s2 = s1;
            s1 = temp;
        }
        return ((double)(s1.length() - editDistance(s1, s2))) / (double)(s1.length());
    }

    // Levenshtein Edit Distance
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
        int lastValue = i;
        for (int j = 0; j <= s2.length(); j++) {
            if (i == 0)
            costs[j] = j;
            else {
            if (j > 0) {
                int newValue = costs[j - 1];
                if (s1.charAt(i - 1) != s2.charAt(j - 1))
                newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                costs[j - 1] = lastValue;
                lastValue = newValue;
            }
            }
        }
        if (i > 0)
            costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
