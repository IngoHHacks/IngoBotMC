package tv.ingoh.minecraft.plugins.ingobotcore.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

import net.md_5.bungee.api.ChatColor;

public class ChatHistory {

    private LinkedList<HistoryEntry> history = new LinkedList<>();

    public void append(String string, boolean isPublic, String user, String model, boolean finish, boolean isInput) {
        if (isPublic) history.add(new HistoryEntry(string.endsWith("\n") ? string : string + "\n", "*", model, finish, isInput));
        else history.add(new HistoryEntry(string.endsWith("\n") ? string : string + "\n", user, model, finish, isInput));
        if (history.size() > 100) history.removeFirst();
    }

    public String getHistory(boolean isPublic, String user, int limit) {
        String hist = "";
        for (HistoryEntry entry : history) {
            if (isPublic) {
                if (entry.user.equals("*")) hist += entry.string.replace("\n", "\r\n");
            } else {
                if (entry.user.equals(user)) hist += entry.string.replace("\n", "\r\n");
            }
        }
        if (hist.endsWith("\r\n")) {
            hist = hist.substring(0, hist.length()-2);
        }
        try {
            hist = URLEncoder.encode(hist.replaceAll("[^\\x00-\\x7F]", ""), "UTF-8").replace("%2F", "%252F");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (hist.length() >= limit) {
            String str = hist.substring(hist.length() - limit, hist.length()).replaceAll("[^\\x00-\\x7F]", "");
            return str.endsWith("\n") ? str : (str.endsWith("\r") ? str + "\n" : str + "\r\n");
        }
        return hist.replaceAll("[^\\x00-\\x7F]", "").replace("%2F", "%252F");
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
                return history.get(i).string.substring(0, history.get(i).string.length()-1);
            }
            i--;
        }
        return "[empty]";
    }

    public HistoryEntry getLastEntry(String user, boolean isPublic) {
        int i = history.size() - 1;
        while (i >= 0) {
            if (history.get(i).user == (isPublic ? "*" : user)) {
                return history.get(i);
            }
            i--;
        }
        return null;
    }

    public void fullUndo(String user, boolean isPublic) {
        HistoryEntry entry = getLastEntry(user, isPublic);
        if (entry.isFinish) {
            remove(user, isPublic, 1);
        } else {
            remove(user, isPublic, 2);
        }
    }

    public String getFormattedHistory(String user, boolean isPublic) {
        int count = 10;
        String str = "";
        try {
            int amt = 0;
            int i = history.size() - 1;
            while (count > 0 && i >= 0) {
                if (history.get(i).user.equals(isPublic ? "*" : user)) {
                    if (!str.equals("")) str = "\n" + str;
                    str = history.get(i).formattedString() + str;
                    amt++;
                    count--;
                }
                i--;
            }
            if (str.equals("")) str = ChatColor.RED + "(EMPTY)";
            return str;
        } catch (Exception e) {
            return ChatColor.RED + "[ERROR]";
        }
    }

	public void appendF(String a, String b, boolean isPublic, String user, String model, boolean finish, boolean isInput) {
        String string = a + b;
        if (isPublic) history.add(new HistoryEntry(string.endsWith("\n") ? string : string + "\n", "*", model, finish, isInput, a, b));
        else history.add(new HistoryEntry(string.endsWith("\n") ? string : string + "\n", user, model, finish, isInput, a, b));
        if (history.size() > 100) history.removeFirst();
	}

}
