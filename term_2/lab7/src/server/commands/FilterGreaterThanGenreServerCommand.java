package server.commands;

import java.util.stream.Collectors;

import exceptions.Messages;
import manager.CollectionManager;
import models.MusicBand;
import models.MusicGenre;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.GenreArgument;

public class FilterGreaterThanGenreServerCommand implements ServerCommand {
    private final CollectionManager cm;

    public FilterGreaterThanGenreServerCommand(CollectionManager cm) { this.cm = cm; }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return cm.withReadLock(() -> {
            try {
                GenreArgument argument = (GenreArgument) request.getArgument();
                MusicGenre genre = argument.getGenre();
                String message = cm.getCollection().values().stream()
                        .filter(b -> b.getGenre().ordinal() > genre.ordinal())
                        .map(MusicBand::toString)
                        .collect(Collectors.joining("\n"));
                if (message.isEmpty()) {
                    message = Messages.COLLECTION_EMPTY;
                }
                return new CommandResponse(true, message, null);
            } catch (ClassCastException | NullPointerException e) {
                return new CommandResponse(false, Messages.commandError("filter_greater_than_genre", Messages.INVALID_GENRE), null);
            }
        });
    }
    @Override 
    public String getDescription(){
        return "вывести элементы, значение поля genre которых больше заданного";
    }
}
