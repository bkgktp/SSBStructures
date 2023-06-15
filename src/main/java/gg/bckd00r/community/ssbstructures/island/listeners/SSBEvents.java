package gg.bckd00r.community.ssbstructures.island.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandSchematicPasteEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.utils.Schematic;
import gg.bckd00r.community.ssbstructures.utils.StructureControl;
import gg.bckd00r.community.ssbstructures.utils.mechanic.WorldEditHook;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.io.File;
import java.util.Optional;

public class SSBEvents implements Listener {



    @EventHandler
    public void islandSchematicPaste(IslandSchematicPasteEvent event) {

        StructureControl structureControl = new StructureControl(event.getIsland());
        structureControl.build();

        //event.getSchematic() == normal keyin ismi structure/normal ...
    }

    @EventHandler
    public void islandDelete(IslandDisbandEvent event) {
        Island island = event.getIsland();

        for (Chunk chunk : island.getAllChunks()) {
            for (Entity entity : chunk.getEntities()) {
                boolean isCitizensNPC = entity.hasMetadata("NPC");
                if (isCitizensNPC) {
                    CitizensAPI.getNPCRegistry().getNPC(entity).destroy();

                }
                //Bukkit.broadcastMessage("hasIslandEntityCitizensMetaData: " + isCitizensNPC);
            }
        }

    }

    //sadece schematic ismini giricem ve çıktı olarak ada bilgilerini vericek
    @EventHandler
    public void testEvent(PlayerToggleSneakEvent event) {

        Player player = event.getPlayer();

        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        Island island = superiorPlayer.getIsland();

        if (island == null)
            return;

        //String file = Bukkit.getServer().getPluginManager().getPlugin("PLUGIN_NAME").getDataFolder();


        File myfile = new File(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit").getDataFolder().getAbsolutePath() + "/palm_tree.schem");


        Bukkit.broadcastMessage(myfile.getAbsolutePath());

        Optional<Schematic> board = Schematic.load(myfile);
        board.get().paste(player.getLocation().add(0,5,0));

        //Schematic schem = new Schematic(board);

        //schem.paste(player.getLocation().add(0,5,0));

        //WorldEditHook.schematicPaste(player, player.getWorld(), player.getLocation(), myfile);

        /*
        for (Chunk chunk : island.getAllChunks()) {
            for (Entity entity : chunk.getEntities()) {
                boolean isCitizensNPC = entity.hasMetadata("NPC");
                Bukkit.broadcastMessage("hasIslandEntityCitizensMetaData: " + isCitizensNPC);
            }
        }
         */

        //StructureControl structureControl = new StructureControl(island);
        //structureControl.build();


        Location maxloc = island.getMaximumProtected();
        Location minloc = island.getMinimumProtected();

        Location clickedLoc = event.getPlayer().getLocation();

        //BlockPosition maximumProtectedPosition = Objects.requireNonNull(superiorPlayer.getIsland()).getMaximumProtectedPosition();
        //BlockPosition minimumProtectedPosition = Objects.requireNonNull(superiorPlayer.getIsland()).getMinimumProtectedPosition();



        Bukkit.broadcastMessage(String.valueOf(island.getCenterPosition()));

        //StructureManager manager = new StructureManager("normal");
        //manager.build();

        /*
        StructureConfig structureConfig = new StructureAPI().getStructure("normal");
        structureConfig.paste();
         */

        //Map<String, Object> section = new ConfigsManager(SSBStructures.get()).getSection().getValues(false);

        //if (section == null)
        //    return;

        /*
        String value = section.getString("normal");
        if (value == null)
            return;

        Map<String, Object> values = section.getValues(false);
         */
        //for (Map.Entry<String, Object> vp : section.entrySet())
        //    player.sendMessage(vp.getKey() + " " + vp.getValue());
        //player.sendMessage(String.valueOf(section.getOrDefault("normal", "")));

    }


}
