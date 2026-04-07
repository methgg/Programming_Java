package manager;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import exceptions.ErrorMessages;
import models.MusicBand;

public class CollectionManager {
    private final LinkedHashMap<Long, MusicBand> collection = new LinkedHashMap<>();
    private final LocalDateTime initializationDate = LocalDateTime.now();

    public void insert(Long key, MusicBand band) {
        collection.put(key, band);
    }

    public void remove(Long key) {
        collection.remove(key);
    }

    public void show() {
        if (collection.isEmpty()) {
            System.out.println(ErrorMessages.COLLECTION_EMPTY);
        } else {
            collection.forEach((k, v) -> System.out.println(k + " -> " + v));
        }
    }

    public void clear() {
        collection.clear();
    }

    public int size() {
        return collection.size();
    }

    public LinkedHashMap<Long, MusicBand> getCollection() {
        return collection;
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }
}
