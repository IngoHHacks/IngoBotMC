package tv.ingoh.minecraft.plugins.ingobotcore.web;

import java.util.LinkedList;

public class ChatHistory {

    private LinkedList<HistoryEntry> history = new LinkedList<>();

    public void append(String string, boolean isPublic, String user) {
        if (isPublic) history.add(new HistoryEntry(string, "*"));
        else history.add(new HistoryEntry(string, user));
        if (history.size() > 50) history.removeFirst();
    }

    public String getHistory(boolean isPublic, String user) {
        String hist = "";
        for (HistoryEntry entry : history) {
            if (isPublic) {
                if (entry.user.equals("*")) hist += entry.string.replace("\n", "\r\n");
            } else {
                if (entry.user.equals(user)) hist += entry.string.replace("\n", "\r\n");
            }
            if (hist.length() >= 1000) return hist.substring(0, 1000).replaceAll("[^\\x00-\\x7F]", "");
        }
        return hist.replaceAll("[^\\x00-\\x7F]", "");
    }

    public void removeLast() {
        if (history.size() > 0) history.removeLast();
    }

}
