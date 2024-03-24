package gg.bckd00r.community.ssbstructures.utils.mechanic;


import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import gg.bckd00r.community.ssbstructures.utils.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class WorldEditHook {

    private final Island island;
    private ConfigurationSection schematicSection;

    public WorldEditHook(Island island) {
        this.island = island;
        ConfigsManager manager = SSBStructures.getConfigsManager();
        if (manager == null)
            return;

        ConfigurationSection section = manager.getStructureSection(); //structures*
        ConfigurationSection dependSection = section.getConfigurationSection(island.getSchematicName()); //normalin içindeyiz şu an
        if (dependSection == null)
            return;

        this.schematicSection = dependSection.getConfigurationSection("WorldEdit");

    }

    public void paste(String key) {
        if (schematicSection.isConfigurationSection(key)) {
            ConfigurationSection configData = schematicSection.getConfigurationSection(key);
            if (configData == null)
                return;

            Location configLocation = configData.getLocation("location");
            String schemName = configData.getString("name", "palm_tree.schem");
            if (configLocation == null) {
                System.out.println("ERROR: SSBStructures/WorldEdit/" + key + " has location data is error!");
                return;
            }
            configLocation.setWorld(island.getCenterPosition().getWorld());

            Location center = island.getCenterPosition().getBlock().getLocation();
            Location spawnLoc = center.clone().add(configLocation);
            spawnLoc.setWorld(island.getMaximumProtected().getWorld());

            File myfile = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("SSBStructures")).getDataFolder().getAbsolutePath() + "/schematics/" + schemName);

            Optional<Schematic> board = Schematic.load(myfile);
            board.get().paste(spawnLoc.getBlock().getLocation());

        }
    }


    public void pasteAll() {
        for (String schemSectionName : schematicSection.getKeys(false)) {
            paste(schemSectionName);

        }
    }
}



