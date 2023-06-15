package gg.bckd00r.community.ssbstructures.api;

import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class StructureManager {

    private List<String> schemList;


    public StructureType getType() {
        return type;
    }

    private StructureType type;


    private String schemName;

    private final Island island;

    //private final World world;

    private String name;

    //private final FileConfiguration fileConfiguration;


    public StructureManager(Island island) {
        this.island = island;

        //SuperiorSkyblockAPI.getFactory().createPlayerBuilder().build();
        //this.world = world;

        /*
        for (String schemNames : structureSection.getKeys(false)) {
            ConfigurationSection schemSection = structureSection.getConfigurationSection(schemNames);
            if (schemSection != null) {
                Bukkit.getConsoleSender().sendMessage(schemSection.getName());
            }
        }
         */
    }

    public void build() {
        FileConfiguration file = SSBStructures.get().getConfig();
        if (!file.isConfigurationSection("structures"))
            return;

        ConfigurationSection structureSection = file.getConfigurationSection("structures");
        if (structureSection == null)
            return;

        ConfigurationSection schemObjectNames = structureSection.getConfigurationSection(schemName); //island_schematic_name
        if (schemObjectNames == null)
            return;

        for (String listSchemName : schemObjectNames.getKeys(false)) { //schemtic-mob-npc

            Bukkit.getConsoleSender().sendMessage(listSchemName);
            //Bukkit.getConsoleSender().sendMessage(listSchemName + " " + this.schemName);

            ConfigurationSection structures = schemObjectNames.getConfigurationSection(listSchemName);
            if (structures == null)
                continue; //bozuk değeri döngüden çıkarır

            this.type = StructureType.valueOf(listSchemName.toUpperCase());

            //ortak datalar coordinate-name-type

            //StructureSummon structureSummon = new StructureSummon(structures); //mob-schematic-npc-tree
            //structureSummon.summon();

        }
                    //this.type = StructureType.valueOf(objectSection.getString("type"));
                    //this.location = objectSection.getLocation("location");

                    /*
                    if (type == StructureType.SCHEMATIC) {
                        String name = objectSection.getString("name", "example");
                        this.structure = new StructureObject(type, location, name); //schematic
                    } else if (type == StructureType.MOB ){

                    }

                     */

                    //StructureObject structure = new StructureObject()
                    /*
                    for (Player player : Bukkit.getOnlinePlayers()) {

                        objectSection.set("location", player.getLocation());
                    }

                     */
                    //SSBStructures.get().saveConfig();

                    /*
                    if (type == StructureType.NPC) {
                        this.structureConfig = new StructureObject(type, )

                    } else if (type == StructureType.MOB) {

                    }
                     */


            //objectSection.set(objectSection.getCurrentPath(), );
            //Bukkit.getConsoleSender().sendMessage(Objects.requireNonNull(objectSection.getString("name")));

    }



    public String getSchemName() {
        return schemName;
    }


    public Island getIsland() {
        return island;
    }

    //public World getWorld() {
        //return world;
    //}
}
