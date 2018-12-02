package com;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.naming.NoPermissionException;
import java.io.IOException;
import java.nio.charset.Charset;

class CommandManager {

    private final String BUG_CHAR = String.valueOf((char) 13);

    private final KarmaPoints plugin;

    private final YamlConfiguration config;

    private PointService pointService;

    private CommandSender sender;

    CommandManager(KarmaPoints plugin) {
        this.plugin = plugin;
        this.pointService = new PointServiceImpl(plugin);
        this.config = plugin.getConfiguration();
    }

    boolean execute(CommandSender sender, String[] args) throws NoSuchPlayerException, NoPermissionException, DelayException, IOException, SelfUseException {
        this.sender = sender;
        if (args.length != 0)
            switch (args[0]) {
                case "time":
                case "top":
                case "help":
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

    private boolean executeCommand(String command) throws NoPermissionException, IOException {
        switch (command) {
            case "help":
                message(IOUtils.toString(plugin.getResource("help.txt"), Charset.forName("UTF-8")).replaceAll(BUG_CHAR, ""));
                return true;
            case "time":
                long result = pointService.minutesUntilGainPoint(sender.getName());
                message(result == -1 ? config.getString("no-time-message") : String.format(config.getString("time-message"), result));
                return true;
            case "top":
                message(config.getString("top-message"));
                pointService.topByPoints().forEach(x -> message(String.format(config.getString("top-element-message"), x.nickname, x.points)));
                return true;
            case "adhelp":
                checkOp();
                message(IOUtils.toString(plugin.getResource("admin_help.txt"), Charset.forName("UTF-8")).replaceAll(BUG_CHAR, ""));
                return true;
        }
        return false;
    }

    private boolean executeCommand(String command, String player) throws NoPermissionException, DelayException, SelfUseException {
        switch (command) {
            case "good":
                checkSelf(player);
                message(config.getString("good-message"));
                return pointService.addPoints(sender.getName(), player, 1);
            case "bad":
                checkSelf(player);
                message(config.getString("bad-message"));
                return pointService.addPoints(sender.getName(), player, -1);
            case "display":
                checkOp();
                message(String.format(config.getString("display-message"), pointService.getPoints(player)));
                return true;
        }
        return false;
    }

    private boolean executeCommand(String command, String player, int qty) throws NoPermissionException, DelayException, SelfUseException {
        switch (command) {
            case "rob":
                checkOp();
                message(config.getString("rob-message"));
                pointService.addPoints(player, -qty);
                return true;
            case "gift":
                checkSelf(player);
                boolean success = pointService.transferPoints(sender.getName(), player, qty);
                if (success)
                    message(config.getString("gift-message"));
                else
                    message(config.getString("wrong-gift-message"));
                return true;
            case "tempadd":
                checkOp();
                message(config.getString("no-realization-message"));
                return true; // TODO: 01.12.2018  ???
            case "tempprob":
                checkOp();
                message(config.getString("no-realization-message"));
                return true; // TODO: 01.12.2018  ???
            case "set":
                checkOp();
                message(config.getString("set-message"));
                pointService.setPoints(player, qty);
                return true;
            case "add":
                checkOp();
                message(config.getString("add-message"));
                pointService.addPoints(player, qty);
                return true;
        }
        return true;
    }

    private void checkOp() throws NoPermissionException {
        if (!sender.hasPermission("karmaPoints.op"))
            throw new NoPermissionException();
    }

    private void checkSelf(String player) throws SelfUseException {
        if(sender.getName().equals(player))
            throw new SelfUseException();
    }
}
