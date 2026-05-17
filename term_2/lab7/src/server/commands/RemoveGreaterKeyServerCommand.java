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
import network.arguments.KeyArgument;

public class RemoveGreaterKeyServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final UserRepository userRepository;

    public RemoveGreaterKeyServerCommand(CollectionManager collectionManager,
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
            KeyArgument argument = (KeyArgument) request.getArgument();
            AuthData authData = request.getAuthData();
            Long key = argument.getKey();

            if (authData == null || authData.getUsername() == null || authData.getUsername().isBlank()) {
                return new CommandResponse(false, ErrorMessages.AUTH_DATA_MISSING, null);
            }

            Long userId = userRepository.findUserIdByUsername(authData.getUsername());
            if (userId == null) {
                return new CommandResponse(false, ErrorMessages.AUTH_INVALID, null);
            }

            List<Long> keysToRemove = collectionManager.getCollection().keySet().stream()
                    .filter(existingKey -> existingKey > key)
                    .filter(existingKey -> {
                        try {
                            return musicBandRepository.isOwner(existingKey, userId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            if (keysToRemove.isEmpty()) {
                return new CommandResponse(true, ErrorMessages.removedGreaterKeys(key), null);
            }

            int deletedCount = musicBandRepository.deleteByKeys(keysToRemove);
            if (deletedCount != keysToRemove.size()) {
                return new CommandResponse(false,
                        ErrorMessages.commandExecutionError("Не все элементы удалось удалить из базы данных."),
                        null);
            }

            keysToRemove.forEach(collectionManager::remove);

            return new CommandResponse(true, ErrorMessages.removedGreaterKeys(key), null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false,
                    ErrorMessages.commandError("remove_greater_key", ErrorMessages.INVALID_KEY),
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
        return "удалить из коллекции все элементы, ключ которых превышает заданный";
    }
}
