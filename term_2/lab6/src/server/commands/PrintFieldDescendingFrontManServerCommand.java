package server.commands;

import java.util.Comparator;
import java.util.stream.Collectors;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import models.Person;
import network.CommandRequest;
import network.CommandResponse;

public class PrintFieldDescendingFrontManServerCommand implements ServerCommand {
    private final CollectionManager cm;

    public PrintFieldDescendingFrontManServerCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String message = cm.getCollection().values().stream()
                .map(MusicBand::getFrontMan)
                .sorted(Comparator.comparing(
                        Person::getName,
                        Comparator.nullsLast(String::compareTo)
                ).reversed())
                .map(Person::toString)
                .collect(Collectors.joining("\n"));

        if (message.isEmpty()) {
            message = ErrorMessages.COLLECTION_EMPTY;
        }

        return new CommandResponse(true, message, null);
    }

    @Override
    public String getDescription() {
        return "вывести значения поля frontMan всех элементов в порядке убывания";
    }
}
