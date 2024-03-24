package gg.bckd00r.community.ssbstructures.commands;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import gg.bckd00r.community.ssbstructures.utils.LocationCalc;
import gg.bckd00r.community.ssbstructures.utils.StructureControl;
import gg.bckd00r.community.ssbstructures.utils.mechanic.CitizensHook;

import gg.bckd00r.community.ssbstructures.utils.mechanic.MythicMobsHook;
import gg.bckd00r.community.ssbstructures.utils.mechanic.OraxenHook;
import gg.bckd00r.community.ssbstructures.utils.mechanic.WorldEditHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommandsManager {


    public void loadCommands() {
        new CommandAPICommand("ssbstructures")
                .withAliases("ss", "ssbs")
                .withPermission("ssbs.command")
                .withSubcommands(getCreateRegionCommand(), getSummonCommand(), getLocationGetCommand(), getSetLocationCommand(), reloadCommand())

                .executes((sender, args) -> {
                    sender.sendMessage("komut yanlış veya eksik!");
                }).register();
    }

    private CommandAPICommand reloadCommand() {
        return new CommandAPICommand("reloadconfig")
                .withPermission("ssbs.command.admin")
                .executes((sender, args) -> {
                    SSBStructures.get().reloadConfigManager();
                    sender.sendMessage("Config reloaded!3");
                });
    }

    private CommandAPICommand getLocationGetCommand() {
        return new CommandAPICommand("getlocation")
                .withPermission("ssbs.command.admin")
                .executes((sender, args) -> {

                    if (sender instanceof Player player) {

                        Island island = getSenderIsland(player);
                        if (island == null)
                            return;

                        Location distanceVectorLocation = LocationCalc.distanceCalculator(island, player.getLocation());
                        player.sendMessage(distanceVectorLocation.toString());

                    }

                });
    }

    //ss createregion name
    private CommandAPICommand getCreateRegionCommand() {
        return new CommandAPICommand("createregion")
                .withPermission("ssbs.command.admin")
                .withArguments(new GreedyStringArgument("regionName"))
                .executes((sender, args) -> {

                    if (sender instanceof Player player) {

                        Island island = getSenderIsland(player);
                        if (island == null)
                            return;

                        Location selectLoc = null;
                        World world = BukkitAdapter.adapt(Objects.requireNonNull(player.getLocation().getWorld()));
                        //BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
                        WorldEditPlugin worldEditPlugin = ((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"));
                        if (worldEditPlugin != null) {

                            try {
                                BlockVector3 vectorMaxLocation = worldEditPlugin.getSession(player).getSelection(world).getMaximumPoint();

                                BlockVector3 vectorMinLocation = worldEditPlugin.getSession(player).getSelection(world).getMinimumPoint();

                                Location maxLoc = new Location(player.getWorld(), vectorMaxLocation.getX() + 1, vectorMaxLocation.getY(), vectorMaxLocation.getZ() + 1);
                                Location minLoc = new Location(player.getWorld(), vectorMinLocation.getX(), vectorMinLocation.getY(), vectorMinLocation.getZ());

                                Location distancedMaxLoc = LocationCalc.distanceCalculator(island, maxLoc);
                                Location distancedMinLoc = LocationCalc.distanceCalculator(island, minLoc);


                                ConfigurationSection regionSection = ConfigsManager.getRegionNameSection(island.getSchematicName());
                                if (regionSection == null)
                                    return;

                                if (args.count() == 1) {
                                    String regionName = (String) args.get("regionName");

                                    if (regionName == null)
                                        return;

                                    ConfigurationSection newRegionSection = regionSection.createSection(regionName);

                                    newRegionSection.set("maxloc", distancedMaxLoc);
                                    newRegionSection.set("minloc", distancedMinLoc);

                                    newRegionSection.set("region-interaction-permission", "island.region.interaction");
                                    SSBStructures.get().saveConfig();
                                }

                            } catch (IncompleteRegionException e) {
                                throw new RuntimeException(e);
                            }

                        }

                        player.sendMessage("create new region!");

                    }

                });
    }

    private CommandAPICommand getSetLocationCommand() {

        ConfigsManager manager = SSBStructures.getConfigsManager();
        if (manager == null)
            return null;

        ConfigurationSection structureSection = manager.getStructureSection();
        if (structureSection == null)
            return null;

        return new CommandAPICommand("setlocation")
                .withPermission("ssbs.command.location")
                //.withOptionalArguments(new LocationArgument("location"))

                .withArguments(
                        //new TextArgument("type").replaceSuggestions(ArgumentSuggestions.strings("set", "get")),
                        new TextArgument("schematics").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                            return structureSection.getKeys(false).toArray(new String[0]);
                        })),

                        new TextArgument("type").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                            // Use info.previousArgs() to access the previous arguments (targets, SchematicName, and StructureType)
                            //Collection<Player> targets = (Collection<Player>) info.previousArgs().get("targets");
                            String schematicName = (String) info.previousArgs().get("schematics");
                            if (schematicName == null)
                                return null;
                            //String schematicName = getSenderIsland((Player) info.sender());
                            List<String> values = getStructureTypeList(schematicName);
                            if (values == null)
                                return null;

                            return values.toArray(new String[0]);
                        })),

                        new TextArgument("inside").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                            // Use info.previousArgs() to access the previous arguments (targets, SchematicName, and StructureType)
                            //Collection<Player> targets = (Collection<Player>) info.previousArgs().get("targets");
                            //String schematicName = Objects.requireNonNull(getSenderIsland((Player) info.sender())).getSchematicName();
                            String schematicName = (String) info.previousArgs().get("schematics");
                            if (schematicName == null)
                                return null;

                            String structureType = (String) info.previousArgs().get("type");
                            List<String> values = getStructureData(schematicName, structureType);
                            if (values == null)
                                return null;

                            return values.toArray(new String[0]);
                        }))

                )
                .withOptionalArguments(new LocationArgument("location"))
                .executes((sender, args) -> {
                    if (sender instanceof Player player) {
                        Island island = getSenderIsland(player);
                        if (island == null)
                            return;

                        Location selectLoc = null;
                        World world = BukkitAdapter.adapt(Objects.requireNonNull(player.getLocation().getWorld()));
                        //BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
                        WorldEditPlugin worldEditPlugin = ((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"));
                        if (worldEditPlugin != null) {

                            try {
                                BlockVector3 vectorBlock = worldEditPlugin.getSession(player).getSelection(world).getBoundingBox().getPos1();
                                selectLoc = new Location(player.getWorld(), vectorBlock.getBlockX(), vectorBlock.getBlockY(), vectorBlock.getBlockZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                            } catch (IncompleteRegionException e) {
                                throw new RuntimeException(e);
                            }

                        }

                        if (args.count() == 3) {
                            if (selectLoc == null) {
                                selectLoc = player.getLocation();

                            }

                            Location distancedLocation = LocationCalc.distanceCalculator(island, selectLoc);
                            String schematicsString = (String) args.get("schematics");
                            if (schematicsString == null)
                                return;

                            ConfigurationSection schematics = structureSection.getConfigurationSection(schematicsString); //oraxen-citizens v.b
                            if (schematics == null)
                                return;

                            String typeString = (String) args.get("type");
                            if (typeString == null)
                                return;

                            ConfigurationSection type = schematics.getConfigurationSection(typeString); //furniture1
                            if (type == null)
                                return;

                            String insideString = (String) args.get("inside");
                            if (insideString == null)
                                return;

                            ConfigurationSection inside = type.getConfigurationSection(insideString); //furniture1
                            if (inside == null)
                                return;

                            inside.set("location", distancedLocation);
                            SSBStructures.get().saveConfig();

                            player.sendMessage("saved location! " + distancedLocation);

                        }
                    }
                });
    }



    /*
                .withArguments(new EntitySelectorArgument.ManyPlayers("targets"),
                        new TextArgument("item")
                               .replaceSuggestions(ArgumentSuggestions.strings(OraxenItems.getItemNames())),
                        new IntegerArgument("amount"))
     */
    //ss (set or summon) summon ise oyuncu ismi lazım (island name = normal) (all veya structure type)

    //ss summon bckd00r normal (all-structure name) (all veya structure içindeki verileri)
    //

    private List<String> getStructureTypeList(String key) {
        ConfigsManager manager = SSBStructures.getConfigsManager();
        if (manager == null)
            return null;

        ConfigurationSection structureSection = manager.getStructureSection();
        if (structureSection == null)
            return null;

        ConfigurationSection dependSec = structureSection.getConfigurationSection(key);
        if (dependSec == null)
            return null;
        return dependSec.getKeys(false).stream().toList();

    }

    private List<String> getStructureData(String key, String key2) {
        ConfigsManager manager = SSBStructures.getConfigsManager();
        if (manager == null)
            return null;

        ConfigurationSection structureSection = manager.getStructureSection(); //normal
        if (structureSection == null)
            return null;

        ConfigurationSection dependSec = structureSection.getConfigurationSection(key); //oraxen-citizens v.b
        if (dependSec == null)
            return null;

        ConfigurationSection dependInside = dependSec.getConfigurationSection(key2); //furniture-1
        if (dependInside == null)
            return null;


        return dependInside.getKeys(false).stream().toList();

    }

    public static Island getSenderIsland(@NotNull Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if (superiorPlayer == null)
            return null;

        return superiorPlayer.getIsland();
    }

    private CommandAPICommand getSummonCommand() {
        ConfigsManager manager = SSBStructures.getConfigsManager();
        if (manager == null)
            return null;

        ConfigurationSection structureSection = manager.getStructureSection();
        if (structureSection == null)
            return null;


        return new CommandAPICommand("summon")
                .withPermission("ssbs.command.summon")
                .withOptionalArguments(
                        new EntitySelectorArgument.ManyPlayers("targets"),
                        //new TextArgument("SchematicName").replaceSuggestions(ArgumentSuggestions.strings(structureSection.getKeys(false).stream().toList())),
                        //new TextArgument("StructureType").replaceSuggestions(ArgumentSuggestions.strings("Oraxen", "WorldEdit", "MythicMobs", "Citizens", "all")),

                        new TextArgument("StructureType").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                            // Use info.previousArgs() to access the previous arguments (targets, SchematicName, and StructureType)
                            //Collection<Player> targets = (Collection<Player>) info.previousArgs().get("targets");
                            //String schematicName = (String) info.previousArgs().get("SchematicName");
                            Island island = getSenderIsland((Player) info.sender());
                            if (island == null)
                                return null;
                            //String schematicName = getSenderIsland((Player) info.sender());
                            List<String> values = getStructureTypeList(island.getSchematicName());
                            if (values == null)
                                return null;

                            return values.toArray(new String[0]);
                        })),

                        new TextArgument("StructureTypeInside").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                            // Use info.previousArgs() to access the previous arguments (targets, SchematicName, and StructureType)
                            //Collection<Player> targets = (Collection<Player>) info.previousArgs().get("targets");
                            //String schematicName = Objects.requireNonNull(getSenderIsland((Player) info.sender())).getSchematicName();
                            Island island = getSenderIsland((Player) info.sender());
                            if (island == null)
                                return null;

                            String structureType = (String) info.previousArgs().get("StructureType");
                            List<String> values = getStructureData(island.getSchematicName(), structureType);
                            if (values == null)
                                return null;

                            return values.toArray(new String[0]);
                        }))

                ).executes((sender, args) -> {
                            Island island = getSenderIsland((Player) sender);
                            if (island == null)
                                return;
                            StructureControl control = new StructureControl(island);
                            //ss summon bckd00r
                            if (args.count() == 1) {
                                control.pasteAll();
                                sender.sendMessage("Island Structure: " + args.get(1) + " " + args.get(2) + " pasted!");

                                //ss summon bckd00r citizens
                            } else if (args.count() == 2) {
                                String structureType = (String) args.get("StructureType");
                                if (structureType == null)
                                    return;

                                control.selectDependSection(structureType);
                                sender.sendMessage("Island Structure: island:" + args.get(1) + " schematic:" + args.get(2) + " type:" + args.get(3) + " pasted!");

                                //ss summon bckd00r oraxen furniture1
                            } else if (args.count() == 3) {

                                String structureType = (String) args.get("StructureType"); //arg-3 worldedit-oraxen-citizens-mythicmobs
                                if (structureType == null)
                                    return;

                                String lastArg = (String) args.get("StructureTypeInside");
                                if (lastArg == null)
                                    return;

                                ConfigurationSection dependSec = structureSection.getConfigurationSection(island.getSchematicName()); //worldedit-mythicsmob fln
                                if (dependSec == null)
                                    return;

                                ConfigurationSection dependInside = dependSec.getConfigurationSection(structureType); //furniture-1
                                if (dependInside == null)
                                    return;

                                switch (structureType) {
                                    case "Oraxen" -> {
                                        //OraxenHook oraxenHook = new OraxenHook(dependInside, island);
                                        OraxenHook oraxenHook = new OraxenHook(island);
                                        oraxenHook.paste(lastArg);
                                    }
                                    case "Citizens" -> {
                                        CitizensHook citizensHook = new CitizensHook(island);
                                        citizensHook.paste(lastArg);
                                    }
                                    case "WorldEdit" -> {
                                        WorldEditHook worldEditHook = new WorldEditHook(island);
                                        worldEditHook.paste(lastArg);
                                    }
                                    case "MythicMobs" -> {
                                        MythicMobsHook mythicMobsHook = new MythicMobsHook(island);
                                        mythicMobsHook.paste(lastArg);
                                    }
                                }

                                sender.sendMessage("Island Structure: island:" + args.get(1) + " schematic:" + args.get(2) + " type:" + args.get(3) + " pasted!");


                            }
                        }
                );

    }
}