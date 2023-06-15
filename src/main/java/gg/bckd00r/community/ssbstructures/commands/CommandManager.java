package gg.bckd00r.community.ssbstructures.commands;

import gg.bckd00r.community.ssbstructures.SSBStructures;
import gg.bckd00r.community.ssbstructures.config.ConfigsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage("Usage: /ssbs <reloadconfig/setlocation>");
            return true;
        } else {

            if (args[0].equalsIgnoreCase("reloadconfig")) {

                SSBStructures.get().reload();
                //SSBStructures.get().reloadConfig();

                p.sendMessage(ChatColor.GREEN + "Config file has been reloaded!");
                return true;
            }

            //      0    1       2      3
            //ssbs set normal citizens npc1
            if (args[0].equalsIgnoreCase("set")) { //island schematic name

                ConfigsManager manager = SSBStructures.getConfigsManager();
                ConfigurationSection citizenSection = manager.getSchematicNamesSection(args[1]).getConfigurationSection(args[2]);


                if (citizenSection == null) {
                    Bukkit.broadcastMessage("citizenSection error!");
                    return false;
                }

                ConfigurationSection npcValuesSection = citizenSection.getConfigurationSection(args[3]);

                if (npcValuesSection == null) {
                    Bukkit.broadcastMessage("npcValuesSection error!");
                    return false;
                }

                npcValuesSection.set("location", ((Player) sender).getLocation());
                SSBStructures.get().saveConfig();


                sender.sendMessage("added config!");

                /*
                ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
                item.getItemMeta().setDisplayName("Location Tool");
                ((Player) sender).getInventory().addItem(item);

                 */


                //if (args[1].equalsIgnoreCase("")) {}




                //Bukkit.getConsoleSender().sendMessage("Using debug command!");
                //new StructureManager("normal").build();
                //p.sendMessage(ChatColor.GREEN + "Config file has been reloaded!");
                return true;
            }

            if (args[0].equalsIgnoreCase("debug")) {
                Bukkit.getConsoleSender().sendMessage("Using debug command!");
                //new StructureManager("normal").build();
                //p.sendMessage(ChatColor.GREEN + "Config file has been reloaded!");
                return false;
            }


        }

        p.sendMessage(ChatColor.RED + "Invalid command argument!");

        //p.playSound(p.getLocation(), Sound.NOTE_BASS, 100, 1);
            /*
            if (args.length >= 1) {
                String msg = "";
                for (int i = 0; i < args.length; i++) {
                    msg = msg + args[i] + " "
                }

                Bukkit.broadcastMessage("hello world!");
                return true;
            }

             */
        return false;

    }
}
