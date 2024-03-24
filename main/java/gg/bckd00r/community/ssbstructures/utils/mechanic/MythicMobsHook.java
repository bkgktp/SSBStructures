package gg.bckd00r.community.ssbstructures.utils.mechanic;

import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class MythicMobsHook {
    private final Island island;

    private ConfigurationSection mythicMobSection;

    public MythicMobsHook(Island island) {
        this.island = island;
        ConfigsManager manager = SSBStructures.getConfigsManager();
        if (manager == null)
            return;
        ConfigurationSection section = manager.getStructureSection();
        ConfigurationSection dependSection = section.getConfigurationSection(island.getSchematicName());
        if (dependSection == null)
            return;
        this.mythicMobSection = dependSection.getConfigurationSection("MythicMobs");
    }

    public void paste(String key) {
        if (this.mythicMobSection.isConfigurationSection(key)) {
            ConfigurationSection configData = this.mythicMobSection.getConfigurationSection(key);
            if (configData == null)
                return;
            Location configLocation = configData.getLocation("location");
            String mobName = configData.getString("name", "StaticallyChargedSheep");
            int mobCount = configData.getInt("count", 1);
            int mobLevel = configData.getInt("level", 1);
            if (configLocation == null) {
                System.out.println("ERROR: SSBStructures/MythicMobs/" + key + " has location data is error!");
                return;
            }
            configLocation.setWorld(this.island.getCenterPosition().getWorld());
            Location center = this.island.getCenterPosition().getBlock().getLocation();
            Location spawnLoc = center.clone().add(configLocation);
            spawnLoc.setPitch(configLocation.getPitch());
            spawnLoc.setYaw(configLocation.getYaw());
            MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(mobName).orElse(null);
            if (mob != null)
                for (int i = 1; i <= mobCount; i++)
                    mob.spawn(BukkitAdapter.adapt(spawnLoc), mobLevel);
        }
    }

    public void pasteAll() {
        for (String mythicMobSectionName : this.mythicMobSection.getKeys(false))
            paste(mythicMobSectionName);
    }
}
