package dev.gether.getcustomitem.tasks.data;

import dev.gether.getcustomitem.GetCustomItem;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class CobwebLocations {

    private final File LOCATIONS_FILE = new File(GetCustomItem.getInstance().getDataFolder(), "cobweb_locations.dat");
    private Set<CobwebLocationRecord> locations = new HashSet<>();

    @SuppressWarnings("unchecked")
    public CobwebLocations() {
        if (!LOCATIONS_FILE.exists()) {
            saveToFile();
        } else {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(LOCATIONS_FILE))) {
                locations = (Set<CobwebLocationRecord>) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Set<CobwebLocationRecord> getLocations() {
        return locations;
    }

    public void addLocation(CobwebLocationRecord record) {
        locations.add(record);
    }

    public void removeLocation(CobwebLocationRecord record) {
        locations.remove(record);
    }

    public void saveToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(LOCATIONS_FILE))) {
            outputStream.writeObject(locations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
