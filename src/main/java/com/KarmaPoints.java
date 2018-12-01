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
    private YamlConfiguration data;
    File configFile = new File(getDataFolder(), "config.yml");
    File dataFile = new File(getDataFolder(), "data.yml");

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
        if(!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
        }
        data = new YamlConfiguration();
        data.load(dataFile);
        config = new YamlConfiguration();
        config.load(configFile);
    }

    public YamlConfiguration getConfiguration() {
        return config;
    }

    public YamlConfiguration getData() {
        return data;
    }

    public void writeData(String key, Object value) {
        data.set(key, value);
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}