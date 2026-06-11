package server.commands;

import java.sql.SQLException;

import database.MusicBandRepository;
import exceptions.Messages;
import manager.CollectionManager;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.KeyArgument;
import server.AuthService;


public class RemoveKeyServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final AuthService authService;

    public RemoveKeyServerCommand(CollectionManager collectionManager, MusicBandRepository musicBandRepository, AuthService authService) {
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

                if (!collectionManager.getCollection().containsKey(key)) {
                    return new CommandResponse(false, Messages.elementNotFound(key), null);
                }

                if (!musicBandRepository.isOwner(key, userId)) {
                    return new CommandResponse(false, Messages.ACCESS_DENIED, null);
                }

                boolean deleted = musicBandRepository.deleteByKey(key);
                if (!deleted) {
                    return new CommandResponse(false,
                            Messages.commandExecutionError("Объект не удалось удалить из базы данных."),
                            null);
                }

                collectionManager.remove(key);
                return new CommandResponse(true, Messages.removedByKey(key), null);
            } catch (ClassCastException | NullPointerException e) {
                return new CommandResponse(false,
                        Messages.commandError("remove_key", Messages.INVALID_KEY),
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
        return "удалить элемент из коллекции по его ключу";
    }
}
