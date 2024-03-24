package gg.bckd00r.community.ssbstructures.utils.mechanic;

import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class CitizensHook {

    private final Island island;
    private ConfigurationSection citizensSection;

    public CitizensHook(Island island) {
        this.island = island;


        ConfigsManager manager = SSBStructures.getConfigsManager();
        if (manager == null)
            return;

        ConfigurationSection section = manager.getStructureSection(); //structures*
        ConfigurationSection dependSection = section.getConfigurationSection(island.getSchematicName()); //normalin içindeyiz şu an
        if (dependSection == null)
            return;

        this.citizensSection = dependSection.getConfigurationSection("Citizens");

    }


    public void paste(String key) {
        if (citizensSection.isConfigurationSection(key)) {
            ConfigurationSection configData = citizensSection.getConfigurationSection(key);
            if (configData == null)
                return;

            String name = configData.getString("name", "citizens_" + island.getName() + "_npc");
            EntityType type = EntityType.valueOf(configData.getString("type", "PLAYER"));

            Location configLocation = configData.getLocation("location");

            List<String> commands = configData.getStringList("commands");
            if (configLocation == null) {
                System.out.println("ERROR: SSBStructures/Citizens/" + key + " has location data is error!");
                return;
            }
            configLocation.setWorld(island.getCenterPosition().getWorld());

            Location center = island.getCenterPosition().getBlock().getLocation();
            Location spawnLoc = center.clone().add(configLocation);
            spawnLoc.setPitch(configLocation.getPitch());
            spawnLoc.setYaw(configLocation.getYaw());

            NPC npc = CitizensAPI.getNPCRegistry().createNPC(type, name);
            for (String c : commands) {
                String cmd = c.replace("%npc%", String.valueOf(npc.getId()));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
            npc.spawn(spawnLoc);
        }

    }

            //coordinate = new Coordinate(coordSection);

    public void pasteAll() {
        for (String NpcSectionName : citizensSection.getKeys(false)) {
            paste(NpcSectionName);
        }

    }



    public Island getIsland() {
        return island;
    }
}
