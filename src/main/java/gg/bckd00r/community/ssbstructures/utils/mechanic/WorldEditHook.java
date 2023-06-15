package gg.bckd00r.community.ssbstructures.utils.mechanic;


import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.utils.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Optional;

public class WorldEditHook {

    private final Island island;
    private final ConfigurationSection schematicSection;

    public WorldEditHook(ConfigurationSection section, Island island) {
        this.schematicSection = section;
        this.island = island;

    }

    public void summon() {
        for (String schemSectionName : schematicSection.getKeys(false)) {
            if (schematicSection.isConfigurationSection(schemSectionName)) {
                ConfigurationSection configData = schematicSection.getConfigurationSection(schemSectionName);
                if (configData == null)
                    continue;

                Location configLocation = configData.getLocation("location");


                String schemName = configData.getString("name", "palm_tree.schem");
                if (configLocation == null) {
                    System.out.println("ERROR: SSBStructures/WorldEdit/" + schemSectionName + " has location data is error!");
                    continue;
                }

                Location center = island.getCenterPosition().getBlock().getLocation();
                Location spawnLoc = center.clone().add(configLocation);

                File myfile = new File(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit").getDataFolder().getAbsolutePath() + "/schematics/" + schemName);

                //Bukkit.broadcastMessage(myfile.getAbsolutePath());

                Optional<Schematic> board = Schematic.load(myfile);
                board.get().paste(spawnLoc.getBlock().getLocation());

            }
        }
    }



}

