package com;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.NoPermissionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class KarmaPoints extends JavaPlugin {

    private YamlConfiguration config;
    private YamlConfiguration tempData;
    private YamlConfiguration points;
    private File configFile = new File(getDataFolder(), "config.yml");
    private File dataFile = new File(getDataFolder(), "tempData.yml");
    private File pointsFile = new File(getDataFolder(), "points.yml");

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        try {
            loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getLogger().info("KarmaPoints is running!");
        commandManager = new CommandManager(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (commandManager.execute(sender, args)) {
                tempData.save(dataFile);
                points.save(pointsFile);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPlayerException e) {
            sender.sendMessage(config.getString("no-permission-message"));
        } catch (NoPermissionException e) {
            sender.sendMessage(config.getString("no-player-message"));
        } catch (DelayException e) {
            sender.sendMessage(String.format(config.getString("delay-message"), e.getHOW_LONG()));
        }
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadConfig() throws IOException, InvalidConfigurationException {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            Files.copy(getResource("default_config.yml"), configFile.toPath());
        }

        if (!dataFile.exists()) {
            dataFile.createNewFile();
        }

        if (!pointsFile.exists()) {
            pointsFile.createNewFile();
        }

        tempData = new YamlConfiguration();
        tempData.load(dataFile);
        config = new YamlConfiguration();
        config.load(configFile);
        points = new YamlConfiguration();
        points.load(pointsFile);
    }

    YamlConfiguration getConfiguration() {
        return config;
    }

    YamlConfiguration getPoints() {
        return points;
    }

    YamlConfiguration getTempData() {
        return tempData;
    }
}