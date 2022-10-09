package net.ingoh.util;

public class Escape {
    
    public static String escape(String string) {
        try {
            return string.replace("\\", "\\\\").replace("\"", "\\\"");
        } catch (Exception e) {
            return string;
        }
    }

    public static String escapeNewLines(String string) {
        try {
            return string.replace("\n", "\\n");
        } catch (Exception e) {
            return string;
        }
    }

    public static String escapeAll(String string) {
        return escapeNewLines(escape(string));
    }

}