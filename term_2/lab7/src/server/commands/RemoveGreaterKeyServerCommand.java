package server.commands;

import java.sql.SQLException;
import java.util.List;

import database.MusicBandRepository;
import exceptions.Messages;
import manager.CollectionManager;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.KeyArgument;
import server.AuthService;

public class RemoveGreaterKeyServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final AuthService authService;

    public RemoveGreaterKeyServerCommand(CollectionManager collectionManager,
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
                KeyArgument argument = (KeyArgument) request.getArgument();
                AuthData authData = request.getAuthData();
                Long key = argument.getKey();
                Long userId = authService.requireUserId(authData);

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
                    return new CommandResponse(true, Messages.NO_OWNED_ELEMENTS_TO_REMOVE, null);
                }

                int deletedCount = musicBandRepository.deleteByKeys(keysToRemove);
                if (deletedCount != keysToRemove.size()) {
                    return new CommandResponse(false,
                            Messages.commandExecutionError("Не все элементы удалось удалить из базы данных."),
                            null);
                }

                keysToRemove.forEach(collectionManager::remove);

                return new CommandResponse(true, Messages.OWNED_ELEMENTS_GREATER_KEYS_REMOVED, null);
            } catch (ClassCastException | NullPointerException e) {
                return new CommandResponse(false,
                        Messages.commandError("remove_greater_key", Messages.INVALID_KEY),
                        null);
            } catch (IllegalStateException e) {
                return new CommandResponse(false, e.getMessage(), null);
            } catch (SQLException e) {
                return new CommandResponse(false,
                        Messages.commandExecutionError(e.getMessage()),
                        null);
            } catch (RuntimeException e) {
                return new CommandResponse(false,
                        Messages.commandExecutionError(e.getMessage()),
                        null);
            }
        });
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, ключ которых превышает заданный";
    }
}
