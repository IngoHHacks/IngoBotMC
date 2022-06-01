package tv.ingoh.minecraft.plugins.ingobotcore.web;

import java.util.LinkedList;

public class ChatHistory {

    private LinkedList<HistoryEntry> history = new LinkedList<>();

    public void append(String string, boolean isPublic, String user) {
        if (isPublic) history.add(new HistoryEntry(string.endsWith("\n") ? string : string + "\n", "*"));
        else history.add(new HistoryEntry(string.endsWith("\n") ? string : string + "\n", user));
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
        }
        if (hist.length() >= 1000) {
            String str = hist.substring(hist.length() - 1000, hist.length()).replaceAll("[^\\x00-\\x7F]", "");
            return str.endsWith("\n") ? str : (str.endsWith("\r") ? str + "\n" : str + "\r\n");
        }
        return hist.replaceAll("[^\\x00-\\x7F]", "");
    }

    public void removeLast() {
        if (history.size() > 0) history.removeLast();
    }

    public int remove(String user, boolean isPublic, int count) {
        try {
            int removed = 0;
            int i = history.size() - 1;
            while (count > 0 && i >= 0) {
                if (history.get(i).user.equals(isPublic ? "*" : user)) {
                    history.remove(i);
                    removed++;
                    count--;
                }
                i--;
            }
            return removed;
        } catch (Exception e) {
            return -1;
        }
    }

    public String getLast(String user, boolean isPublic) {
        int i = history.size() - 1;
        while (i >= 0) {
            if (history.get(i).user == (isPublic ? "*" : user)) {
                return history.get(i).string;
            }
            i--;
        }
        return "[empty]";
    }

}
