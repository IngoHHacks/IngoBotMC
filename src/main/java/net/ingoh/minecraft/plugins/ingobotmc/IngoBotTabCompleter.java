package net.ingoh.minecraft.plugins.ingobotmc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.ingoh.util.calculator.Calculator;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class IngoBotTabCompleter implements TabCompleter {

    public final static String[] COMMANDS = {
        "69",
        "bold",
        "c",
        "calc",
        "countdown",
        "clearhistory",
        "cum",
        "eyes",
        "f",
        "fs",
        "hidestats",
        "history",
        "how",
        "image",
        "italic",
        "obfuscated",
        "ping",
        "printmap",
        "retry",
        "rng",
        "rq",
        "s",
        "statistic",
        "swc",
        "strikethrough",
        "underlined",
        "undohistory",
        "userinfo",
        "wikisearch"
    };

    final List<String> COUNTDOWNVALUES = List.of("3", "5", "10", "30", "60");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getLabel().equals("i")) {
            if (args.length == 1) {
                return finishCommand(args[0], sender);
            } else {
                return finishArgs(args, sender);
            }
        }
        return null;
    }

    public final static String[] SWC_NORMAL = {
        "claim",
        "get",
        "rewards"
    };

    public final static String[] SWC_OP = {
        "add",
        "remove",
        "softadd",
        "softremove"
    };

    public final static String[] SWC_CLAIM = {
        "milestones"
    };

    private List<String> finishArgs(String[] args, CommandSender sender) {
        LinkedList<String> possible = new LinkedList<>();
        switch (args[0]) {
            case "calc":
            return finishCalc(args);
            case "countdown":
            if (args.length == 2) {
                for (String val : COUNTDOWNVALUES) {
                    if (val.startsWith(args[1])) possible.add(val);
                }
                for (String val : COUNTDOWNVALUES) {
                    if (val.contains(args[1]) && !possible.contains(val)) possible.add(val);
                }
                return possible;
            }
            else return null;
            case "swc":
            if (args.length == 2) {
                LinkedList<String> allP = new LinkedList<>(Arrays.asList(SWC_NORMAL));
                if (sender.isOp()) {
                    allP.addAll(Arrays.asList(SWC_OP));
                }
                for (String val : allP) {
                    if (val.startsWith(args[1])) possible.add(val);
                }
                for (String val : allP) {
                    if (val.contains(args[1]) && !possible.contains(val)) possible.add(val);
                }
                return possible;
            } else if (args.length == 3 && args[1].toLowerCase().equals("claim")) {
                for (String val : SWC_CLAIM) {
                    if (val.startsWith(args[2])) possible.add(val);
                }
                for (String val : SWC_CLAIM) {
                    if (val.contains(args[2]) && !possible.contains(val)) possible.add(val);
                }
                return possible;
            } else return null;
            default:
            return null;
        }
    }

    private List<String> finishCommand(String string, CommandSender sender) {
        LinkedList<String> possible = new LinkedList<>();
        for (String cmd : COMMANDS) {
            if (cmd.startsWith(string)) {
                possible.add(cmd);
            }
        }
        for (String cmd : COMMANDS) {
            if (cmd.contains(string) && !possible.contains(cmd)) {
                possible.add(cmd);
            }
        }
        return possible;
    }

    private List<String> finishCalc(String[] args) {
        LinkedList<String> possible = new LinkedList<>();
        // TODO: Use StringBuilder
        String flattened = "";
        for (String s : args) {
            flattened += s;
        }
        int brLeft = StringUtils.countMatches(flattened, "(");
        int brRight = StringUtils.countMatches(flattened, ")");
        if (brLeft > brRight) {
            for (int i = 1; i <= (brLeft - brRight); i++) {
                possible.add(args[args.length-1] + ")".repeat(i));
            }
        }

        if (args[args.length-1].length() > 0 && Character.isLetter(args[args.length-1].charAt(args[args.length-1].length()-1))) {
            int k = 0;
            for (int i = args[args.length-1].length() - 1; i >= 0; i--) {
                if (!Character.isLetter(args[args.length-1].charAt(i))) {
                    k = i + 1;
                    break;
                }
            }
            for (String v : Calculator.getVarList().getList()) {
                if (v.startsWith(args[args.length-1].substring(k))) possible.add(args[args.length-1].substring(0,k) + v);
            }
        } else {
            Calculator.getVarList().getList().forEach(v -> possible.add(args[args.length-1] + v));
        }

        return possible;
    }

}
