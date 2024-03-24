package gg.bckd00r.community.ssbstructures.utils;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Schematic {

    private final Clipboard clipboard;

    public Schematic(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    public Clipboard getClipboard() {
        return clipboard;
    }

    public void paste(org.bukkit.Location target) {
        World world = BukkitAdapter.adapt(target.getWorld());
        Location location = BukkitAdapter.adapt(target);

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);

        Operation operation = new ClipboardHolder(clipboard).createPaste(session)
                .to(location.toVector().toBlockPoint()).ignoreAirBlocks(true).build();

        try {
            Operations.complete(operation);

            session.flushSession();
        } catch (WorldEditException exception) {
            exception.printStackTrace();
        }
    }

    public static Optional<Schematic> load(File file) {
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            return Optional.empty();
        }

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            return Optional.of(new Schematic(reader.read()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return Optional.empty();
    }
}