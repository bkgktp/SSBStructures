package gg.bckd00r.community.ssbstructures.config;

import gg.bckd00r.community.ssbstructures.SSBStructures;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigsManager {

    private ConfigurationSection structureSection;

    public ConfigsManager(SSBStructures plugin) {
        if (plugin.getConfig().isConfigurationSection("structures")) {
            structureSection = plugin.getConfig().getConfigurationSection("structures");
        }

    }

    public boolean isIslandHaveAnConfig(String name) {
        for (String configIslandSchematicNames : structureSection.getKeys(false)) {
            if (configIslandSchematicNames.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ConfigurationSection getSchematicNamesSection(String name) {
        if (structureSection.isConfigurationSection(name)) {
            return structureSection.getConfigurationSection(name);
        }
        return null;
    }


}
