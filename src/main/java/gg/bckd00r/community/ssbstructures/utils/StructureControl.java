package gg.bckd00r.community.ssbstructures.utils;

import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import gg.bckd00r.community.ssbstructures.utils.mechanic.CitizensHook;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class StructureControl {

    public ConfigsManager manager;

    private final Island island;

    public static ConfigurationSection dependList;

    public StructureControl(Island island) {
        this.manager = SSBStructures.get().getConfigManager();
        this.island = island;

        if (manager.isIslandHaveAnConfig(island.getSchematicName())) {
            dependList = manager.getSchematicNamesSection(island.getSchematicName());

            //normalin içindeki bütün datalar worldedit-citizens-mythicmob

        }
    }

    public void build() {
        if (dependList == null)
            return;
        for (String NAME : dependList.getKeys(false)) {


            /*
            ConfigurationSection nesnedSection = schematicNameSection.getConfigurationSection(NAME);
            if (nesnedSection == null)
                return;
            */

            if (NAME.equals("Citizens") && Bukkit.getPluginManager().isPluginEnabled("Citizens")) {

                //npc1-npc2
                ConfigurationSection citizensSection = dependList.getConfigurationSection("Citizens");
                if (citizensSection == null)
                    continue;

                CitizensHook citizensHook = new CitizensHook(island, citizensSection);
                citizensHook.summon();

                //Bukkit.broadcastMessage("citizens addon is enabled!"); //citizens,hmcleaves,mythicmob ...

            } else if (NAME.equals("MythicMobs") && Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
                Bukkit.broadcastMessage("mythicmobs is enabled"); //citizens,hmcleaves,mythicmob ...


            } else if (NAME.equals("WorldEdit") && Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                Bukkit.broadcastMessage("worldedit is enabled"); //citizens,hmcleaves,mythicmob ...

                //} else if (NAME.equals("HMCLeaves") && Bukkit.getPluginManager().isPluginEnabled("HMCLeaves")) {
                //    Bukkit.broadcastMessage("hmcleaves is enabled"); //citizens,hmcleaves,mythicmob ...
                //}
            }
        }
    }

    public Island getIsland() {
        return island;
    }
}
