package server.commands;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import database.MusicBandRepository;
import exceptions.Messages;
import manager.CollectionManager;
import network.CollectionElement;
import network.CommandRequest;
import network.CommandResponse;

public class ShowServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;

    public ShowServerCommand(CollectionManager collectionManager, MusicBandRepository musicBandRepository) {
        this.collectionManager = collectionManager;
        this.musicBandRepository = musicBandRepository;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return collectionManager.withReadLock(() -> {
            try {
                LinkedHashMap<Long, CollectionElement> sortedCollection = musicBandRepository.loadAllElementsWithOwners()
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(entry -> entry.getValue().getMusicBand().getName()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (first, second) -> first,
                            LinkedHashMap::new
                    ));

                if (sortedCollection.isEmpty()) {
                    return new CommandResponse(true, Messages.COLLECTION_EMPTY, sortedCollection);
                }

                String message = sortedCollection.entrySet().stream()
                        .map(entry -> entry.getKey() + " -> " + entry.getValue().getMusicBand()
                                + " owner=" + entry.getValue().getOwnerUsername())
                        .collect(Collectors.joining("\n"));

                return new CommandResponse(true, message, sortedCollection);
            } catch (SQLException e) {
                return new CommandResponse(false, Messages.databaseReadError(e.getMessage()), null);
            }
        });
    }

    @Override
    public String getDescription() {
        return "вывести элементы коллекции, отсортированные по имени";
    }
}
