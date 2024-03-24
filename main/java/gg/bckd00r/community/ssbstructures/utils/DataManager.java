package gg.bckd00r.community.ssbstructures.utils;

import gg.bckd00r.community.ssbstructures.SSBStructures;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DataManager {

    // Özel anahtar oluşturucu
    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(SSBStructures.get(), key);
    }

    // Oyuncunun verisini kaydetmek için bu fonksiyonu kullanabilirsiniz
    public static void setPlayerData(Player player, String key, boolean value) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        dataContainer.set(getKey(key), PersistentDataType.INTEGER, value ? 1 : 0);
    }

    // Oyuncunun verisini almak için bu fonksiyonu kullanabilirsiniz
    public static boolean getPlayerData(Player player, String key) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        return dataContainer.getOrDefault(getKey(key), PersistentDataType.INTEGER, 0) == 1;
    }

}
