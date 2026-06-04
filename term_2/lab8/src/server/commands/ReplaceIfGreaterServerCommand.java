package server.commands;

import java.sql.SQLException;

import database.MusicBandRepository;
import exceptions.Messages;
import manager.CollectionManager;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.InsertArgument;
import server.AuthService;

public class ReplaceIfGreaterServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final AuthService authService;

    public ReplaceIfGreaterServerCommand(CollectionManager collectionManager,
                                        MusicBandRepository musicBandRepository,
                                        AuthService authService) {
        this.collectionManager = collectionManager;
        this.musicBandRepository = musicBandRepository;
        this.authService = authService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return collectionManager.withWriteLock(() -> {
            try {
                InsertArgument argument = (InsertArgument) request.getArgument();
                AuthData authData = request.getAuthData();

                Long key = argument.getKey();
                MusicBand newBand = argument.getMusicBand();
                Long userId = authService.requireUserId(authData);

                MusicBand oldBand = collectionManager.getCollection().get(key);
                if (oldBand == null) {
                    return new CommandResponse(false, Messages.elementNotFound(key), null);
                }

                if (!musicBandRepository.isOwner(key, userId)) {
                    return new CommandResponse(false, Messages.ACCESS_DENIED, null);
                }

                if (newBand.compareTo(oldBand) <= 0) {
                    return new CommandResponse(true, Messages.NEW_ELEMENT_NOT_GREATER, null);
                }

                MusicBand bandToPersist = new MusicBand(
                        oldBand.getId(),
                        newBand.getName(),
                        newBand.getCoordinates(),
                        oldBand.getCreationDate(),
                        newBand.getNumberOfParticipants(),
                        newBand.getGenre(),
                        newBand.getFrontMan()
                );

                boolean updated = musicBandRepository.updateByKey(key, bandToPersist);
                if (!updated) {
                    return new CommandResponse(false,
                            Messages.commandExecutionError("Объект не удалось обновить в базе данных."),
                            null);
                }

                collectionManager.getCollection().put(key, bandToPersist);

                return new CommandResponse(true, Messages.ELEMENT_REPLACED, null);
            } catch (ClassCastException | NullPointerException e) {
                return new CommandResponse(false,
                        Messages.commandError("replace_if_greater", Messages.INSERT_PARSE_ERROR),
                        null);
            } catch (IllegalStateException e) {
                return new CommandResponse(false, e.getMessage(), null);
            } catch (SQLException e) {
                return new CommandResponse(false,
                        Messages.commandExecutionError(e.getMessage()),
                        null);
            }
        });
    }

    @Override
    public String getDescription() {
        return "заменить значение по ключу, если новое значение больше старого";
    }
}
