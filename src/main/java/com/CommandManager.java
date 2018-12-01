package com;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager {

    private final KarmaPoints plugin;

    private PointService pointService;

    private CommandSender sender;

    public CommandManager(KarmaPoints plugin) {
        this.plugin = plugin;
        this.pointService = new PointServiceImpl(plugin);
    }

    public boolean execute(CommandSender sender, Command command, String label, String[] args) throws NoSuchPlayerException {
        this.sender = sender;
        if (args.length != 0)
            switch (args[0]) {
                case "time":
                case "top":
                case "adhelp":
                    return executeCommand(args[0]);
                case "good":
                case "bad":
                case "display":
                    return args.length == 2 && executeCommand(args[0], standardName(args[1]));
                case "rob":
                case "gift":
                case "tempadd":
                case "tempprob":
                case "set":
                case "add":
                    try {
                        return args.length == 3 && executeCommand(args[0], standardName(args[1]), Integer.valueOf(args[2]));
                    } catch (NumberFormatException e) {
                        return false;
                    }
            }
        return false;
    }

    private void message(String msg) {
        sender.sendMessage(msg);
    }

    private String standardName(String nickname) throws NoSuchPlayerException {
        Player player = plugin.getServer().getPlayer(nickname);
        if (player == null)
            throw new NoSuchPlayerException();
        return player.getName();
    }

    private boolean executeCommand(String command) {
        switch (command) {
            case "time":
                message(pointService.minutesUntilGainPoint(sender.getName()) + " minutes.");
                return true;
            case "top":
                pointService.topByPoints().forEach(this::message);
                return true;
            case "adhelp": // Админская команда
                // TODO: 01.12.2018 Сделать
                return true;
        }
        return false;
    }

    private boolean executeCommand(String command, String player) {
        switch (command) {
            case "good":
                message("executed 'good' command");
                return pointService.addPoints(sender.getName(), player, 1);
            case "bad":
                message("executed 'bad' command");
                return pointService.addPoints(sender.getName(), player, -1);
            case "display": // Админская команда
                message(pointService.getPoints(player) + " points");
                return true;
        }
        return false;
    }

    private boolean executeCommand(String command, String player, int qty) {
        switch (command) {
            case "rob": // Админская команда
                return true;
            case "gift":
                return pointService.transferPoints(sender.getName(), player, qty);
            case "tempadd": // Админская команда
                return true; // TODO: 01.12.2018  ???
            case "tempprob": // Админская команда
                return true; // TODO: 01.12.2018  ???
            case "set": // Админская команда
                pointService.setPoints(player, qty);
                return true;
            case "add": // Админская команда
                pointService.addPoints(player, qty);
                return true;
        }
        return true;
    }
}
