package gg.bckd00r.community.ssbstructures.utils.mechanic;



import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WorldEditHook {


    public static void schematicPaste(Player player, World world, Location location, File file) {

        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(world);

        ClipboardFormat format = ClipboardFormats.findByFile(file);

        if (format != null) {
            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {

                Clipboard clipboard = reader.read();

                try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,
                        -1)) {

                    Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                            .to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ())).ignoreAirBlocks(true).build();

                    try {
                        Operations.complete(operation);
                        editSession.close();

                    } catch (WorldEditException e) {
                        player.sendMessage(ChatColor.RED + "OOPS! Something went wrong, please contact an administrator");
                        e.printStackTrace();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

