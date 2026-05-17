package server.commands;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import database.MusicBandRepository;
import database.UserRepository;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.UpdateArgument;


public class UpdateServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final UserRepository userRepository;

    public UpdateServerCommand(CollectionManager collectionManager,
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
            UpdateArgument argument = (UpdateArgument) request.getArgument();
            AuthData authData = request.getAuthData();

            Long id = argument.getId();
            MusicBand newBand = argument.getMusicBand();

            if (authData == null || authData.getUsername() == null || authData.getUsername().isBlank()) {
                return new CommandResponse(false, ErrorMessages.AUTH_DATA_MISSING, null);
            }

            Long userId = userRepository.findUserIdByUsername(authData.getUsername());
            if (userId == null) {
                return new CommandResponse(false, ErrorMessages.AUTH_INVALID, null);
            }

            Optional<Map.Entry<Long, MusicBand>> existingEntry = collectionManager.getCollection().entrySet().stream()
                    .filter(entry -> entry.getValue().getId().equals(id))
                    .findFirst();

            if (existingEntry.isEmpty()) {
                return new CommandResponse(false, ErrorMessages.elementWithIdNotFound(id), null);
            }

            if (!musicBandRepository.isOwnerByBandId(id, userId)) {
                return new CommandResponse(false, ErrorMessages.ACCESS_DENIED, null);
            }

            MusicBand oldBand = existingEntry.get().getValue();

            MusicBand bandToPersist = new MusicBand(
                    id,
                    newBand.getName(),
                    newBand.getCoordinates(),
                    oldBand.getCreationDate(),
                    newBand.getNumberOfParticipants(),
                    newBand.getGenre(),
                    newBand.getFrontMan()
            );

            boolean updated = musicBandRepository.updateById(id, bandToPersist);

            if (!updated) {
                return new CommandResponse(false,
                        ErrorMessages.commandExecutionError("Объект не удалось обновить в базе данных."),
                        null);
            }

            collectionManager.getCollection().put(existingEntry.get().getKey(), bandToPersist);


            return new CommandResponse(true, ErrorMessages.updatedById(id), null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false,
                    ErrorMessages.commandError("update", ErrorMessages.INVALID_ID),
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
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
