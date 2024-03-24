package gg.bckd00r.community.ssbstructures.regions;

import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegionChecker {

    private ConfigurationSection regionSection;

    private ArrayList<Region> regionList;
    public static Map<String, List<Region>> islandRegions;

    public RegionChecker(Island island) {

        ConfigurationSection schemSection = ConfigsManager.getRegionNameSection(island.getSchematicName());
        if (schemSection != null) {
            for (String regionName : schemSection.getKeys(false)) {
                ConfigurationSection regionSection = schemSection.getConfigurationSection(regionName);
                if (regionSection != null) {
                    Region yeniRegion = new Region(island, regionSection);

                    Bukkit.broadcastMessage(yeniRegion.getRegionEnteringPermission());
                    if (regionList != null) {
                        regionList.add(yeniRegion);

                        islandRegions.put(island.getSchematicName(), regionList);
                    }
                }
            }
        }
    }



    //public Map<String, List<Region>> getIslandRegionsMap() {
    //    return islandRegions;
    //}

    public ArrayList<Region> getIslandRegions() {
        return regionList;
    }

    public boolean isInsideOfRegion(Player player) {
        return true;
    }
}
