package com;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager {

    private final KarmaPoints plugin;

    private PointService pointService = new PointService();

    private CommandSender sender;

    public CommandManager(KarmaPoints plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, Command command, String label, String[] args) throws NoSuchPlayerException {
        this.sender = sender;
        if (args.length == 0)
            args = new String[]{"help"};


        switch (args[0]) {
            case "time": //
                return true;
            case "top": //
                return true;
            case "adhelp": //
                return true;
            case "good": // player
                return true;
            case "bad": // player
                return true;
            case "display": // player
                return true;
            case "gift": // player num
                return true;
            case "add": // player num
                return true;
            case "rob": // player num
                return true;
            case "tempadd": // player num
                return true;
            case "tempprob": // player num
                return true;
            case "set": // player num
                if (args.length < 3)
                    return false;
                plugin.writeData(normalName(args[1]), args[2]);
                return true;
        }
        return false;
    }

    private void message(String msg) {
        sender.sendMessage(msg);
    }

    private String normalName(String nickname) throws NoSuchPlayerException {
        Player player = plugin.getServer().getPlayer(nickname);
        if (player == null)
            throw new NoSuchPlayerException();
        return player.getName();
    }


}
