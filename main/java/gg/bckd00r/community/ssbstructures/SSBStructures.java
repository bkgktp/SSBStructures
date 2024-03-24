package gg.bckd00r.community.ssbstructures;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import gg.bckd00r.community.ssbstructures.commands.CommandsManager;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import gg.bckd00r.community.ssbstructures.island.listeners.SSBEvents;
import gg.bckd00r.community.ssbstructures.regions.RegionsListener;
import gg.bckd00r.community.ssbstructures.utils.listeners.Events;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SSBStructures extends JavaPlugin {

    public static SSBStructures instance;

    public static ConfigsManager configsManager;

    public static ConfigsManager getConfigsManager() {
        return configsManager;
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        instance = this;

        reloadConfigManager();
        new CommandsManager().loadCommands();
        Bukkit.getPluginManager().registerEvents(new SSBEvents(), this);
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(new RegionsListener(), this);

        //new RegionsListener();

    }

    public void reloadConfigManager() {
        if (getDataFolder().exists()) {
            reloadConfig();
            configsManager = new ConfigsManager(instance);

        } else saveResource("config.yml", false);

    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        // Plugin shutdown logic
    }

    public ConfigsManager getConfigManager() {
        return configsManager;
    }
    
    public static SSBStructures get() {
        return instance;
    }


    
}
