package gg.bckd00r.community.ssbstructures.utils.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Events implements Listener {



    @EventHandler
    public void toolInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("ssbs.admin")) {
            ItemStack itemStack = event.getItem();
            if (itemStack == null)
                return;
            ItemMeta meta = itemStack.getItemMeta();
            if ((itemStack.getType() == Material.BLAZE_ROD) && meta.getDisplayName().equals("Location Tool")) {
                if (event.getAction().isRightClick()) {

                    //player.sendMessage(event.getClickedBlock().getLocation());
                    //xyz-orjinal xyz-config

                }
            }
        }

    }
}
