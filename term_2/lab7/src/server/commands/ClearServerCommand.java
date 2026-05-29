package server.commands;

import java.sql.SQLException;
import java.util.List;

import database.MusicBandRepository;
import exceptions.Messages;
import manager.CollectionManager;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import server.AuthService;

public class ClearServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final AuthService authService;

    public ClearServerCommand(CollectionManager collectionManager,
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
                AuthData authData = request.getAuthData();
                Long userId = authService.requireUserId(authData);

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
                        deletedCount > 0 ? Messages.OWNED_ELEMENTS_CLEARED : Messages.NO_OWNED_ELEMENTS_TO_REMOVE,
                        null);
            } catch (SQLException e) {
                return new CommandResponse(false,
                        Messages.commandExecutionError(e.getMessage()),
                        null);
            } catch (IllegalStateException e) {
                return new CommandResponse(false, e.getMessage(), null);
            } catch (RuntimeException e) {
                return new CommandResponse(false,
                        Messages.commandExecutionError(e.getMessage()),
                        null);
            }
        });
    }

    @Override
    public String getDescription() {
        return "очистить все принадлежащие текущему пользователю элементы коллекции";
    }
}
