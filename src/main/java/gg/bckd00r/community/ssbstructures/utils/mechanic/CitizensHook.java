package gg.bckd00r.community.ssbstructures.utils.mechanic;

import com.bgsoftware.superiorskyblock.api.island.Island;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.List;

public class CitizensHook {

    private final Island island;
    private final ConfigurationSection citizensSection;

    private ConfigurationSection configData;


    //--citizens
    //name
    //type
    //display-name
    //cordinate

    public CitizensHook(ConfigurationSection section, Island island) {
        this.citizensSection = section;
        this.island = island;

    }

            //coordinate = new Coordinate(coordSection);

    public void summon() {
        for (String NpcSectionName : citizensSection.getKeys(false)) {
            if (citizensSection.isConfigurationSection(NpcSectionName)) {
                ConfigurationSection configData = citizensSection.getConfigurationSection(NpcSectionName);
                if (configData == null)
                    continue;

                String name = configData.getString("name", "citizens_" + island.getName() + "_npc");
                EntityType type = EntityType.valueOf(configData.getString("type", "PLAYER"));

                Location configLocation = configData.getLocation("location");

                List<String> commands = configData.getStringList("commands");
                if (configLocation == null) {
                    System.out.println("ERROR: SSBStructures/Citizens/" + NpcSectionName + " has location data is error!");
                    continue;
                }

                Location center = island.getCenterPosition().getBlock().getLocation();
                Location spawnLoc = center.clone().add(configLocation);

                NPC npc = CitizensAPI.getNPCRegistry().createNPC(type, name);
                for (String c : commands) {
                    String cmd = c.replace("%npc%", String.valueOf(npc.getId()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
                npc.spawn(spawnLoc);
            }
        }
    }



    public Island getIsland() {
        return island;
    }
}
