package server.commands;

import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

import database.MusicBandRepository;
import database.UserRepository;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.KeyArgument;


public class RemoveKeyServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final UserRepository userRepository;

    public RemoveKeyServerCommand(CollectionManager collectionManager, MusicBandRepository musicBandRepository, UserRepository userRepository) {
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

            if (!collectionManager.getCollection().containsKey(key)) {
                return new CommandResponse(false, ErrorMessages.elementNotFound(key), null);
            }

            if (!musicBandRepository.isOwner(key, userId)) {
                return new CommandResponse(false, ErrorMessages.ACCESS_DENIED, null);
            }

            boolean deleted = musicBandRepository.deleteByKey(key);
            if (!deleted) {
                return new CommandResponse(false,
                        ErrorMessages.commandExecutionError("Объект не удалось удалить из базы данных."),
                        null);
            }

            collectionManager.remove(key);
            return new CommandResponse(true, ErrorMessages.removedByKey(key), null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false,
                    ErrorMessages.commandError("remove_key", ErrorMessages.INVALID_KEY),
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
        return "удалить элемент из коллекции по его ключу";
    }
}
