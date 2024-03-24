package gg.bckd00r.community.ssbstructures.regions;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.utils.LocationCalc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.BoundingBox;

import java.util.List;
import java.util.Objects;

public class Region {

    private final String regionInteractionPermission;

    private String regionEnteringPermission;

    private Location maxLoc;

    private Location minLoc;

    private List<String> enterCommand;

    private List<String> enterFirstCommand;

    private List<String> leaveCommand;

    private BoundingBox boundingBox;

    private final Island island;

    private String regionName;

    public Region(Island island, ConfigurationSection section) {
        this.island = island;

        //regionEnteringPermission = section.getString("region-entering-permission", null);
        regionInteractionPermission = section.getString("region-interaction-permission", "island.region.interaction");

        //if(section.isList("enter-command")) {
        //    enterCommand = section.getStringList("enter-command");
        //}

        if (section.isConfigurationSection("entering")) {
            ConfigurationSection entering = section.getConfigurationSection("entering");
            if (entering == null)
                return;

            if (entering.isString("permission")) {
                regionEnteringPermission = entering.getString("permission", "island.region.enter");
            }
            if (entering.isList("commands")) {
                enterCommand = entering.getStringList("commands");
            }
            if (entering.isList("first-enter-commands")) {
                enterFirstCommand = entering.getStringList("first-enter-commands");
            }
        }

        if (section.isConfigurationSection("leaving")) {
            ConfigurationSection leaving = section.getConfigurationSection("leaving");
            if (leaving == null)
                return;

            if (leaving.isList("commands")) {
                leaveCommand = leaving.getStringList("commands");
            }
        }

        if (section.isLocation("maxloc"))
            maxLoc = section.getLocation("maxloc");

        if (section.isLocation("minloc"))
            minLoc = section.getLocation("minloc");

        Location center = island.getCenterPosition().getBlock().getLocation();
        center.setWorld(island.getMaximum().getWorld());
        if (maxLoc == null)
            return;
        Location maxLocCalculated = center.clone().add(maxLoc);

        if (minLoc == null)
            return;
        Location minLocCalculated = center.clone().add(minLoc);

        boundingBox = BoundingBox.of(minLocCalculated.toVector(), maxLocCalculated.toVector());
    }

    public boolean isInsideRegion(Location location) {
        if (boundingBox == null)
            return false;

        return boundingBox.contains(location.getX(), location.getY(), location.getZ());
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public String getRegionInteractionPermission() {
        return regionInteractionPermission;
    }

    public String getRegionEnteringPermission() {
        return regionEnteringPermission;
    }

    public Location getMaxLocation() {
        return maxLoc;
    }

    public Location getMinLocation() {
        return minLoc;
    }

    public List<String> getEnterCommands() {
        return enterCommand;
    }

    public List<String> getLeaveCommands() {
        return leaveCommand;
    }

    public Island getIsland() {
        return island;
    }

    public List<String> getEnterFirstCommand() {
        return enterFirstCommand;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String name) {
       this.regionName = name;
    }
}

