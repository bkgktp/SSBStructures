package gg.bckd00r.community.ssbstructures.utils;

import com.bgsoftware.superiorskyblock.api.island.Island;
import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import gg.bckd00r.community.ssbstructures.utils.mechanic.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.BoundingBox;

public class StructureControl {
    //public ConfigsManager manager;
    private final Island island;
    public static ConfigurationSection dependList;
    public StructureControl(Island island) {
        this.island = island;

        //this.manager = SSBStructures.get().getConfigManager();
        if (ConfigsManager.isSchematicNameEnough(island.getSchematicName())) { //structures icinde normal varsa
            dependList = ConfigsManager.getSchematicNamesSection(island.getSchematicName());
            //dependList = manager.getSchematicNamesSection(island.getSchematicName());
            //normalin içindeki bütün datalar worldedit-citizens-mythicmob-regions

        }
    }

    public boolean makeCitizens() { //main dependList sectionu girilecek
        if (Bukkit.getPluginManager().isPluginEnabled("Citizens")) {

            CitizensHook citizensHook = new CitizensHook(island);
            citizensHook.pasteAll();
        }
        return true;
    }

    public boolean makeOraxen() { //main dependList sectionu girilecek
        if (Bukkit.getPluginManager().isPluginEnabled("Oraxen")) {

            //ConfigurationSection oraxenHookSection = dependSection.getConfigurationSection("Oraxen");
            //if (oraxenHookSection == null)
            //    return false;

            OraxenHook oraxenHook = new OraxenHook(island);
            oraxenHook.pasteAll();
        }
        return true;
    }

    public boolean makeItemsAdder() { //main dependList sectionu girilecek
        if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {

            //ConfigurationSection oraxenHookSection = dependSection.getConfigurationSection("Oraxen");
            //if (oraxenHookSection == null)
            //    return false;

            ItemsAdderHook itemsAdderHook = new ItemsAdderHook(island);
            itemsAdderHook.pasteAll();
        }
        return true;
    }

    public boolean makeWorldEdit() { //main dependList sectionu girilecek
        if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {

            WorldEditHook worldEditHook = new WorldEditHook(island);
            worldEditHook.pasteAll();
        }
        return true;
    }

    public boolean makeMythicMobs() { //main dependList sectionu girilecek
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {

            MythicMobsHook mythicMobsHook = new MythicMobsHook(island);
            mythicMobsHook.pasteAll();
        }
        return true;
    }

    public void selectDependSection(String key) {
        if (dependList == null)
            return;

        switch (key) {
            case "Citizens" -> this.makeCitizens();
            case "Oraxen" -> this.makeOraxen();
            case "ItemsAdder" -> this.makeItemsAdder();
            case "MythicMobs" -> this.makeMythicMobs();
            case "WorldEdit" -> this.makeWorldEdit();
        }

    }
    public void pasteAll() {
        if (dependList == null)
            return;

        for (String key : dependList.getKeys(false))
            selectDependSection(key);

        //dependList.getKeys(false).forEach(this::selectDependSection);


    }

}


            /*
        for (String NAME : dependList.getKeys(false)) { //citizens-worldedit v.b listeler

            //configde citizens varsa ve pluginlerin arasında citizens varsa
            if (NAME.equals("Citizens")) {

                //BoundingBox
                //npc1-npc2
                makeCitizens(dependList);

            } else if (NAME.equals("MythicMobs")) {

               makeMythicMobs(dependList);

            } else if (NAME.equals("WorldEdit") && Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {

                makeWorldEdit(dependList);

            } else if (NAME.equals("Oraxen") && Bukkit.getPluginManager().isPluginEnabled("Oraxen")) {

                makeOraxen(dependList);

                //Bukkit.broadcastMessage("worldedit is enabled"); //citizens,hmcleaves,mythicmob ...
                //} else if (NAME.equals("HMCLeaves") && Bukkit.getPluginManager().isPluginEnabled("HMCLeaves")) {
                //  Bukkit.broadcastMessage("hmcleaves is enabled"); //citizens,hmcleaves,mythicmob ...
                //}
            }
        }
    }

             */


