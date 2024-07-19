package dev.gether.getcustomitem.tasks;

import com.sk89q.worldguard.protection.flags.Flags;
import dev.gether.getcustomitem.tasks.data.CobwebLocationRecord;
import dev.gether.getcustomitem.tasks.data.CobwebLocations;
import dev.gether.getcustomitem.tasks.data.SerializableLocation;
import dev.gether.getcustomitem.utils.WorldGuardUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class CobwebAutoRemoveTask extends BukkitRunnable {

    @Getter
    private CobwebLocations cobwebLocations;

    private int removeAfter;

    public CobwebAutoRemoveTask(int removeAfter) {
        this.cobwebLocations = new CobwebLocations();
        this.removeAfter = removeAfter;
    }

    @Override
    public void run() {
        Set<CobwebLocationRecord> locations = cobwebLocations.getLocations();
        if (locations.isEmpty()) {
            //nothing to clean, so also don't save to file
            return;
        }
        List<CobwebLocationRecord> forLoop = new ArrayList<>(locations); // copy to avoid ConcurrentModificationException
        //for (CobwebLocationRecord record : forLoop) {
        for (int i = 0; i < forLoop.size(); i++) {
            CobwebLocationRecord record = forLoop.get(i);
            if (isForClean(record.creationTimestamp())) {
                cleanCobweb(record.location(), record.radiusX(), record.radiusY());
                cobwebLocations.removeLocation(record);
            }
        }
        cobwebLocations.saveToFile();
    }

    private boolean isForClean(long creationTimestamp) {
        return System.currentTimeMillis() > creationTimestamp + (removeAfter * 1000L);
    }

    public void cleanCobweb(SerializableLocation sl, int radiusX, int radiusY) {
        Location location = new Location(Bukkit.getWorld(sl.world()), sl.x(), sl.y(), sl.z());
        for (int x = -radiusX + 1; x < radiusX; x++) {
            for (int y = -radiusY + 1; y < radiusY; y++) {
                for (int z = -radiusX + 1; z < radiusX; z++) {
                    Location tempLoc = location.clone().add(x, y, z);
                    if (WorldGuardUtil.isDeniedFlag(tempLoc, null, Flags.BLOCK_BREAK)) {
                        continue;
                    }
                    Block block = tempLoc.getBlock();
                    if (block.getType() == Material.COBWEB) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
