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

    public CitizensHook(Island island, ConfigurationSection section) {
        this.citizensSection = section;
        this.island = island;

        /*
        if (section.isConfigurationSection("coordinate")) {
            ConfigurationSection coordSection = section.getConfigurationSection("coordinate");
            if (coordSection == null)
                return;

         */

        for (String NpcSectionName : citizensSection.getKeys(false)) {
            if (citizensSection.isConfigurationSection(NpcSectionName)) {
                ConfigurationSection configData = citizensSection.getConfigurationSection(NpcSectionName);
                if (configData == null)
                    continue;

                this.configData = configData;


            }
        }
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

                //Bukkit.broadcastMessage(center + "   -----   " + configLocation + "   -------   " + center.clone().add(configLocation));
                //Location centerLocation = new Location(configLocation.getWorld(), center.)
                //CoordinateCalc hesap = new CoordinateCalc(new Coordinate((double) center.getX(), (double) center.getX(), (double) center.getX()), )

                //CitizensAPI.removeNamedNPCRegistry();
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(type, name);
                for (String c : commands) {
                    String cmd = c.replace("%npc%", String.valueOf(npc.getId()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
                npc.spawn(spawnLoc);
            }
        }
        //npc.spawn(spawnLoc);getString
        //Bukkit.broadcastMessage(NpcSectionName);
    }



    public Island getIsland() {
        return island;
    }
}
