package server.commands;

import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

import database.MusicBandRepository;
import database.UserRepository;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.InsertArgument;

public class ReplaceIfGreaterServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final UserRepository userRepository;

    public ReplaceIfGreaterServerCommand(CollectionManager collectionManager,
                                        MusicBandRepository musicBandRepository,
                                        UserRepository userRepository) {
        this.collectionManager = collectionManager;
        this.musicBandRepository = musicBandRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Lock writeLock = collectionManager.getLock().writeLock();
        writeLock.lock();
        try {
            InsertArgument argument = (InsertArgument) request.getArgument();
            AuthData authData = request.getAuthData();

            Long key = argument.getKey();
            MusicBand newBand = argument.getMusicBand();

            if (authData == null || authData.getUsername() == null || authData.getUsername().isBlank()) {
                return new CommandResponse(false, ErrorMessages.AUTH_DATA_MISSING, null);
            }

            Long userId = userRepository.findUserIdByUsername(authData.getUsername());
            if (userId == null) {
                return new CommandResponse(false, ErrorMessages.AUTH_INVALID, null);
            }

            MusicBand oldBand = collectionManager.getCollection().get(key);
            if (oldBand == null) {
                return new CommandResponse(false, ErrorMessages.elementNotFound(key), null);
            }

            if (!musicBandRepository.isOwner(key, userId)) {
                return new CommandResponse(false, ErrorMessages.ACCESS_DENIED, null);
            }

            if (newBand.compareTo(oldBand) <= 0) {
                return new CommandResponse(true, ErrorMessages.NEW_ELEMENT_NOT_GREATER, null);
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
                        ErrorMessages.commandExecutionError("Объект не удалось обновить в базе данных."),
                        null);
            }

            collectionManager.getCollection().put(key, bandToPersist);

            return new CommandResponse(true, ErrorMessages.ELEMENT_REPLACED, null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false,
                    ErrorMessages.commandError("replace_if_greater", ErrorMessages.INSERT_PARSE_ERROR),
                    null);
        } catch (SQLException e) {
            return new CommandResponse(false,
                    ErrorMessages.commandExecutionError(e.getMessage()),
                    null);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String getDescription() {
        return "заменить значение по ключу, если новое значение больше старого";
    }
}
