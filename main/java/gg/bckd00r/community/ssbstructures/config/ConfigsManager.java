package gg.bckd00r.community.ssbstructures.config;

import gg.bckd00r.community.ssbstructures.SSBStructures;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class ConfigsManager {

    private static ConfigurationSection structureSection;

    private static ConfigurationSection regionSection;

    public ConfigsManager(SSBStructures plugin) {
        if (plugin.getConfig().isConfigurationSection("structures")) {
            structureSection = plugin.getConfig().getConfigurationSection("structures");
        }

        if (plugin.getConfig().isConfigurationSection("regions")) {
            regionSection = plugin.getConfig().getConfigurationSection("regions");
        }

    }

    public static boolean isSchematicNameEnough(String name) {
        for (String configIslandSchematicNames : structureSection.getKeys(false)) {
            if (configIslandSchematicNames.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static ConfigurationSection getSchematicNamesSection(String name) {
        if (structureSection.isConfigurationSection(name)) {
            return structureSection.getConfigurationSection(name);
        }
        return null;
    }

    public static ConfigurationSection getRegionNameSection(String name) {
        if (regionSection.isConfigurationSection(name)) {
            return regionSection.getConfigurationSection(name);
        }
        return null;
    }

    public ConfigurationSection getStructureSection() {
        return structureSection;
    }

    //public ConfigurationSection getRegionSection() {return regionSection;}
}
