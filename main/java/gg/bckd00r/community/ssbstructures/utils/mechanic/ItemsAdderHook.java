package gg.bckd00r.community.ssbstructures.utils.mechanic;

import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.lone.itemsadder.api.CustomFurniture;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import io.th0rgal.oraxen.api.OraxenFurniture;
import io.th0rgal.oraxen.mechanics.provided.gameplay.furniture.FurnitureMechanic;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;

public class ItemsAdderHook {

    private final Island island;
    private ConfigurationSection oraxenSection;


    //Just island class and furniture name or pasteAll
    public ItemsAdderHook(Island island) {
        this.island = island;

        ConfigsManager manager = SSBStructures.getConfigsManager();
        if (manager == null)
            return;

        ConfigurationSection section = manager.getStructureSection(); //structures*
        ConfigurationSection dependSection = section.getConfigurationSection(island.getSchematicName()); //normalin içindeyiz şu an
        if (dependSection == null)
            return;

        this.oraxenSection = dependSection.getConfigurationSection("Oraxen");
    }

    public void paste(String key) { //key=sandalye,masa
        if (!oraxenSection.isConfigurationSection(key)) {
            return;
        }

        ConfigurationSection configData = oraxenSection.getConfigurationSection(key);
        if (configData == null)
            return;
        String itemID = configData.getString("itemsadder_item", "");

        String blockFaceString = configData.getString("blockface", "UP");
        BlockFace blockFace = BlockFace.valueOf(blockFaceString);

        Location configLocation = configData.getLocation("location");
        if (configLocation == null) {
            System.out.println("ERROR: SSBStructures/Oraxen/" + key + " has location data is null!");
            return;
        }
        configLocation.setWorld(island.getCenterPosition().getWorld());

        Location center = island.getCenterPosition().getBlock().getLocation();
        Location spawnLoc = center.clone().add(configLocation);
        spawnLoc.setPitch(configLocation.getPitch());
        spawnLoc.setYaw(configLocation.getYaw());

        CustomFurniture.spawn(itemID, spawnLoc.getBlock());

        /*
        //FurnitureMechanic furnitureMechanic = FurnitureMechanic.get
        FurnitureMechanic mechanic = OraxenFurniture.getFurnitureMechanic(itemID);
        if (mechanic == null)
            return;

        mechanic.place(spawnLoc, spawnLoc.getYaw(), blockFace);
         */
    }

    public void pasteAll() {
        for (String iaSectionName : oraxenSection.getKeys(false)) { //sandalye-masa çıktısı verir.
            paste(iaSectionName);

        }
    }
}

