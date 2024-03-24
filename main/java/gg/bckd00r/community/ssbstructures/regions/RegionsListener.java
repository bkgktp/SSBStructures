package gg.bckd00r.community.ssbstructures.regions;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandEnterEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import gg.bckd00r.community.ssbstructures.utils.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;

public class RegionsListener implements Listener {

    private ConfigurationSection regionSection;

    //ada içerisindeki boundingboxlar

    //private final ConfigsManager manager;


    public RegionsListener() {
        //regionSection = ConfigsManager.;

        /*
        for (String islandSchematicName : regionSection.getKeys(false)) { //normal-desert-jungle
            // check the region from given location cordinates and check if player inside of region
            //SuperiorSkyblockAPI.getIslandAt()
        }
         */
    }


    //schematiğin ismi yapılandırmada varsa
    public boolean isRegionInside(Player player) {



        /*
        String islandSchematicName = Objects.requireNonNull(SuperiorSkyblockAPI.getIslandAt(playerLocation)).getSchematicName();

        if (manager.isSchematicNameEnough(islandSchematicName)) {


        }
         */
        return false;
    }

    public RegionChecker regionChecker;


    private final Map<String, List<Region>> islandRegions = new HashMap<>();


    @EventHandler
    public void onIslandJoinEvent(IslandEnterEvent event) {

        Island island = event.getIsland();
        String islandSchematicName = island.getSchematicName();

        List<Region> regionList = new ArrayList<>();

        ConfigurationSection nameSection = ConfigsManager.getRegionNameSection(islandSchematicName); //customregion1
        if (nameSection == null)
            return;
        for (String regionName : nameSection.getKeys(false)) {

            ConfigurationSection nameInsideSection = nameSection.getConfigurationSection(regionName);
            if (nameInsideSection == null)
                return;

            Region region = new Region(island, nameInsideSection);
            region.setRegionName(regionName);
            regionList.add(region);
        }
        islandRegions.put(islandSchematicName, regionList);
        
    }

        /*

        Island island = event.getIsland();
        String islandSchematicName = island.getSchematicName();

        ConfigurationSection section = ConfigsManager.getRegionNameSection(islandSchematicName);
        if (section == null)
            return;

        for (String regionNames : section.getKeys(false)) {

            ConfigurationSection listOfRegionSection = section.getConfigurationSection(regionNames);
            regionChecker = new RegionChecker(listOfRegionSection);


        }

         */


    private boolean getPlayerMetadata(Player player, String key) {
        for (MetadataValue metadataValue : player.getMetadata(key)) {
            if (Objects.requireNonNull(metadataValue.getOwningPlugin()).equals(SSBStructures.get())) {
                return metadataValue.asBoolean();
            }
        }
        return false;
    }

    private void setPlayerMetadata(Player player, String key, boolean value) {
        player.setMetadata(key, new FixedMetadataValue(SSBStructures.get(), value));
    }


    @EventHandler
    public void onRegionEnter(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getIslandAt(player.getLocation());

        if (island == null)
            return;

        if (islandRegions == null)
            return;

        List<Region> regions = islandRegions.get(island.getSchematicName());
        if (regions == null)
            return;

        for (Region region : regions) {
            //if (player.isOp())
            //    return;

            String permissions = region.getRegionEnteringPermission();
            boolean isInRegion = region.isInsideRegion(event.getTo());
            if (permissions != null && !player.hasPermission(permissions) && isInRegion) {
                event.setCancelled(true);
                return;
            }

            boolean wasInRegion = getPlayerMetadata(player, "region_" + region.getRegionName() + "_hasEntered");

            if (isInRegion && !wasInRegion) {
                //Bukkit.broadcastMessage(String.valueOf(player.hasPermission(region.getRegionEnteringPermission())));
                boolean firstEnter = DataManager.getPlayerData(player, "region_" + region.getRegionName() + "_hasFirstEnter");
                if (!firstEnter) {
                    List<String> firstEnteringCommands = region.getEnterFirstCommand();
                    if (firstEnteringCommands != null) {
                        for (String c : firstEnteringCommands) {
                            String playerName = c.replace("%player%", player.getDisplayName());
                            String commandEdited = playerName.replace("%region%", region.getRegionName());
                            //String regionName = c.replace("%player%", player.getDisplayName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandEdited);
                        }
                    }

                    DataManager.setPlayerData(player, "region_" + region.getRegionName() + "_hasFirstEnter", true);
                }

                // Oyuncu alana yeni girdiğinde burası çalışır
                List<String> enteringCommands = region.getEnterCommands();
                if (enteringCommands != null) {
                    for (String c : enteringCommands) {
                        String playerName = c.replace("%player%", player.getDisplayName());
                        String commandEdited = playerName.replace("%region%", region.getRegionName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandEdited);
                    }
                }
                //player.sendMessage("Bir bölgeye girdiniz!");
            } else if (!isInRegion && wasInRegion) {
                List<String> leavingCommands = region.getLeaveCommands();
                if (leavingCommands != null) {
                    for (String c : leavingCommands) {
                        String playerName = c.replace("%player%", player.getDisplayName());
                        String commandEdited = playerName.replace("%region%", region.getRegionName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandEdited);
                    }
                }
                // Oyuncu alandan çıktığında burası çalışır
                //player.sendMessage("Bir bölgeden çıktınız!");
            }
            setPlayerMetadata(player, "region_" + region.getRegionName() + "_hasEntered" , isInRegion);
        }
    }
}
