package gg.bckd00r.community.ssbstructures;

import gg.bckd00r.community.ssbstructures.commands.CommandManager;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import gg.bckd00r.community.ssbstructures.island.listeners.SSBEvents;
import gg.bckd00r.community.ssbstructures.utils.listeners.Events;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SSBStructures extends JavaPlugin {

    public static SSBStructures instance;

    public static ConfigsManager configsManager;

    public static ConfigsManager getConfigsManager() {
        return configsManager;
    }

    /*
    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
    }
     */



    @Override
    public void onEnable() {
        instance = this;

        //new CommandParser(this.getResource("command.rdcml")).parse().register("prefix", this);
        //CommandAPI.onEnable(this);
        Objects.requireNonNull(this.getCommand("ssbstructures")).setExecutor(new CommandManager());

        Bukkit.getPluginManager().registerEvents(new SSBEvents(), this);
        Bukkit.getPluginManager().registerEvents(new Events(), this);


        reload();

        //Objects.requireNonNull(this.getCommand("ssbstructure")).setExecutor(new CommandManager());
        //createCustomConfig();
        /*
        ConfigurationSection structureSection = this.getConfig().getConfigurationSection("structures");
        if (structureSection == null) {
            this.saveResource("config.yml", true);
        }
         */
        //configsManager = new ConfigsManager(this);

    }

    public void reload() {
        if (getDataFolder().exists()) {
            reloadConfig();
            configsManager = new ConfigsManager(this);

        } else saveResource("config.yml", false);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigsManager getConfigManager() {
        return configsManager;
    }
    
    public static SSBStructures get() {
        return instance;
    }
    
    
}
