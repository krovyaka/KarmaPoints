package com;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class KarmaPoints extends JavaPlugin {

    private YamlConfiguration config;
    private YamlConfiguration tempData;
    private YamlConfiguration points;
    File configFile = new File(getDataFolder(), "config.yml");
    File dataFile = new File(getDataFolder(), "tempData.yml");
    File pointsFile = new File(getDataFolder(), "points.yml");

    private CommandManager commandManager = new CommandManager(this);

    @Override
    public void onEnable() {
        try {
            loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getLogger().info("KarmaPoints is running!");
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            return commandManager.execute(sender, command, label, args);
        } catch (NoSuchPlayerException e) {
            sender.sendMessage("This player doesn't exist!");
        }
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadConfig() throws IOException, InvalidConfigurationException {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            config = new YamlConfiguration();
            config.save(configFile);
        }
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
        }
        if (!pointsFile.exists()) {
            pointsFile.getParentFile().mkdirs();
            pointsFile.createNewFile();
        }
        tempData = new YamlConfiguration();
        tempData.load(dataFile);
        config = new YamlConfiguration();
        config.load(configFile);
        points = new YamlConfiguration();
        points.load(pointsFile);
    }

    public YamlConfiguration getConfiguration() {
        return config;
    }

    public YamlConfiguration getTempData() {
        return tempData;
    }

    public void writeData(String key, Object value) {
        tempData.set(key, value);
        try {
            tempData.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getPoints() {
        return points;
    }
}