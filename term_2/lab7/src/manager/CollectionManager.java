package manager;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import exceptions.Messages;
import models.MusicBand;


/**
 * Класс - менеджер коллекции, реализующий основные методы
 */
public class CollectionManager {
    private final LinkedHashMap<Long, MusicBand> collection = new LinkedHashMap<>();
    private final LocalDateTime initializationDate = LocalDateTime.now();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void insert(Long key, MusicBand band) {
        collection.put(key, band);
    }

    public void remove(Long key) {
        collection.remove(key);
    }

    public void show() {
        if (collection.isEmpty()) {
            System.out.println(Messages.COLLECTION_EMPTY);
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

    public <T> T withReadLock(Supplier<T> action) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            return action.get();
        } finally {
            readLock.unlock();
        }
    }

    public void withReadLock(Runnable action) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            action.run();
        } finally {
            readLock.unlock();
        }
    }

    public <T> T withWriteLock(Supplier<T> action) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            return action.get();
        } finally {
            writeLock.unlock();
        }
    }

    public void withWriteLock(Runnable action) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            action.run();
        } finally {
            writeLock.unlock();
        }
    }
}
