package server.commands;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.CommandRequest;
import network.CommandResponse;

public class ShowServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;

    public ShowServerCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        LinkedHashMap<Long, MusicBand> sortedCollection = collectionManager.getCollection().entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getName()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (first, second) -> first,
                        LinkedHashMap::new
                ));

        if (sortedCollection.isEmpty()) {
            return new CommandResponse(true, ErrorMessages.COLLECTION_EMPTY, sortedCollection);
        }

        String message = sortedCollection.entrySet().stream()
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.joining("\n"));

        return new CommandResponse(true, message, sortedCollection);
    }

    @Override
    public String getDescription() {
        return "вывести элементы коллекции, отсортированные по имени";
    }
}
