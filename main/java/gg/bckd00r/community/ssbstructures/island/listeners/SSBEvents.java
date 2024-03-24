package gg.bckd00r.community.ssbstructures.island.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandSchematicPasteEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import dev.lone.itemsadder.api.CustomStack;
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
        //event.getIsland().getOwner().asPlayer()

        StructureControl structureControl = new StructureControl(event.getIsland());
        structureControl.pasteAll();


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
            }
        }
    }

    public void testEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        Island island = superiorPlayer.getIsland();

        if (island == null)
            return;

        //String file = Bukkit.getServer().getPluginManager().getPlugin("PLUGIN_NAME").getDataFolder();


        /*
        File myfile = new File(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit").getDataFolder().getAbsolutePath() + "/palm_tree.schem");


        Bukkit.broadcastMessage(myfile.getAbsolutePath());

        Optional<Schematic> board = Schematic.load(myfile);
        board.get().paste(player.getLocation().add(0,5,0));

         */

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

    }

}
