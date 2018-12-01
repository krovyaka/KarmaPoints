package com;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.naming.NoPermissionException;

public class CommandManager {

    private final KarmaPoints plugin;

    private PointService pointService;

    private CommandSender sender;

    public CommandManager(KarmaPoints plugin) {
        this.plugin = plugin;
        this.pointService = new PointServiceImpl(plugin);
    }

    public boolean execute(CommandSender sender, Command command, String label, String[] args) throws NoSuchPlayerException, NoPermissionException, DelayException {
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

    private boolean executeCommand(String command) throws NoPermissionException {
        switch (command) {
            case "time":
                long result = pointService.minutesUntilGainPoint(sender.getName());
                message(result == -1 ? "No delay" : "Delay is " + result + " minutes");
                return true;
            case "top":
                pointService.topByPoints().forEach(this::message);
                return true;
            case "adhelp":
                checkOp();
                // TODO: 01.12.2018 Сделать
                return true;
        }
        return false;
    }

    private boolean executeCommand(String command, String player) throws NoPermissionException, DelayException {
        switch (command) {
            case "good":
                message("executed 'good' command");
                return pointService.addPoints(sender.getName(), player, 1);
            case "bad":
                message("executed 'bad' command");
                return pointService.addPoints(sender.getName(), player, -1);
            case "display":
                checkOp();
                message(pointService.getPoints(player) + " points");
                return true;
        }
        return false;
    }

    private boolean executeCommand(String command, String player, int qty) throws NoPermissionException, DelayException {
        switch (command) {
            case "rob":
                checkOp();
                pointService.addPoints(player, -qty);
                return true;
            case "gift":
                return pointService.transferPoints(sender.getName(), player, qty);
            case "tempadd":
                checkOp();
                return true; // TODO: 01.12.2018  ???
            case "tempprob":
                checkOp();
                return true; // TODO: 01.12.2018  ???
            case "set":
                checkOp();
                pointService.setPoints(player, qty);
                return true;
            case "add":
                checkOp();
                pointService.addPoints(player, qty);
                return true;
        }
        return true;
    }

    private void checkOp() throws NoPermissionException {
        if(!sender.hasPermission("karmaPoints.op"))
            throw new NoPermissionException();
    }
}
