package server.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.Lock;

import database.MusicBandRepository;
import database.UserRepository;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;

public class ClearServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final UserRepository userRepository;

    public ClearServerCommand(CollectionManager collectionManager,
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
            AuthData authData = request.getAuthData();

            if (authData == null || authData.getUsername() == null || authData.getUsername().isBlank()) {
                return new CommandResponse(false, ErrorMessages.AUTH_DATA_MISSING, null);
            }

            Long userId = userRepository.findUserIdByUsername(authData.getUsername());
            if (userId == null) {
                return new CommandResponse(false, ErrorMessages.AUTH_INVALID, null);
            }

            List<Long> keysToRemove = collectionManager.getCollection().entrySet().stream()
                    .filter(entry -> {
                        try {
                            return musicBandRepository.isOwner(entry.getKey(), userId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(entry -> entry.getKey())
                    .toList();

            int deletedCount = musicBandRepository.deleteAllByOwnerId(userId);
            keysToRemove.forEach(collectionManager::remove);

            return new CommandResponse(true,
                    deletedCount > 0 ? ErrorMessages.COLLECTION_CLEARED : ErrorMessages.COLLECTION_EMPTY,
                    null);
        } catch (SQLException e) {
            return new CommandResponse(false,
                    ErrorMessages.commandExecutionError(e.getMessage()),
                    null);
        } catch (RuntimeException e) {
            return new CommandResponse(false,
                    ErrorMessages.commandExecutionError(e.getMessage()),
                    null);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String getDescription() {
        return "очистить все принадлежащие текущему пользователю элементы коллекции";
    }
}
