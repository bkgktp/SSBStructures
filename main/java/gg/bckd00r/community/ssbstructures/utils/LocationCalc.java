package gg.bckd00r.community.ssbstructures.utils;

import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationCalc {

    public static Location distanceCalculator(Island playerIsland, Location location) {

        double maximumX = playerIsland.getMaximumPosition().getX();
        double maximumY = playerIsland.getMaximumPosition().getY();
        double maximumZ = playerIsland.getMaximumPosition().getZ();

        double playerX = location.getX();
        double playerY = location.getY();
        double playerZ = location.getZ();

        double vectorX;

        if (playerX > maximumX) {
            vectorX = playerIsland.getCenterPosition().getX() - location.getX();
        } else {
            vectorX = location.getX() - playerIsland.getCenterPosition().getX();
        }

        double vectorY = 100;
        if (playerY > playerIsland.getCenterPosition().getY()) { //100'ün üstündeyse pozitif değer vermeli
            vectorY = location.getY() - playerIsland.getCenterPosition().getY();
            //positive
        }

        if (playerY < playerIsland.getCenterPosition().getY()) {
            vectorY = location.getY() - playerIsland.getCenterPosition().getY(); //9
            //negative
        }

        if (playerY == playerIsland.getCenterPosition().getY())
            vectorY = 0;


        double vectorZ;
        if (playerZ > maximumZ) {
            vectorZ = playerIsland.getCenterPosition().getZ() - location.getZ();
        } else {
            vectorZ = location.getZ() - playerIsland.getCenterPosition().getZ();
        }

        return new Location(playerIsland.getMaximumProtected().getWorld(), vectorX, vectorY, vectorZ, location.getYaw(), location.getPitch());
    }
}
