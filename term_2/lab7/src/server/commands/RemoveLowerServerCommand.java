package server.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import database.MusicBandRepository;
import database.UserRepository;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.MusicBandArgument;

public class RemoveLowerServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final UserRepository userRepository;

    public RemoveLowerServerCommand(CollectionManager collectionManager,
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
            MusicBandArgument argument = (MusicBandArgument) request.getArgument();
            AuthData authData = request.getAuthData();
            MusicBand referenceBand = argument.getMusicBand();

            if (authData == null || authData.getUsername() == null || authData.getUsername().isBlank()) {
                return new CommandResponse(false, ErrorMessages.AUTH_DATA_MISSING, null);
            }

            Long userId = userRepository.findUserIdByUsername(authData.getUsername());
            if (userId == null) {
                return new CommandResponse(false, ErrorMessages.AUTH_INVALID, null);
            }

            List<Map.Entry<Long, MusicBand>> entriesToRemove = collectionManager.getCollection().entrySet().stream()
                    .filter(entry -> entry.getValue().compareTo(referenceBand) < 0)
                    .filter(entry -> {
                        try {
                            return musicBandRepository.isOwner(entry.getKey(), userId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            if (entriesToRemove.isEmpty()) {
                return new CommandResponse(true, ErrorMessages.REMOVE_LOWER_DONE, null);
            }

            List<Long> idsToRemove = entriesToRemove.stream().map(entry -> entry.getValue().getId()).toList();

            int deletedCount = musicBandRepository.deleteByBandIds(idsToRemove);
            if (deletedCount != idsToRemove.size()) {
                return new CommandResponse(false,
                        ErrorMessages.commandExecutionError("Не все элементы удалось удалить из базы данных."),
                        null);
            }

            entriesToRemove.stream().map(Map.Entry::getKey).forEach(collectionManager::remove);

            return new CommandResponse(true, ErrorMessages.REMOVE_LOWER_DONE, null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false,
                    ErrorMessages.commandError("remove_lower", ErrorMessages.INSERT_PARSE_ERROR),
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
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
