package server.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import database.MusicBandRepository;
import exceptions.Messages;
import manager.CollectionManager;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.MusicBandArgument;
import server.AuthService;

public class RemoveLowerServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final AuthService authService;

    public RemoveLowerServerCommand(CollectionManager collectionManager,
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
                MusicBandArgument argument = (MusicBandArgument) request.getArgument();
                AuthData authData = request.getAuthData();
                MusicBand referenceBand = argument.getMusicBand();
                Long userId = authService.requireUserId(authData);

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
                    return new CommandResponse(true, Messages.NO_OWNED_ELEMENTS_TO_REMOVE, null);
                }

                List<Long> idsToRemove = entriesToRemove.stream().map(entry -> entry.getValue().getId()).toList();

                int deletedCount = musicBandRepository.deleteByBandIds(idsToRemove);
                if (deletedCount != idsToRemove.size()) {
                    return new CommandResponse(false,
                            Messages.commandExecutionError("Не все элементы удалось удалить из базы данных."),
                            null);
                }

                entriesToRemove.stream().map(Map.Entry::getKey).forEach(collectionManager::remove);

                return new CommandResponse(true, Messages.OWNED_ELEMENTS_LOWER_REMOVED, null);
            } catch (ClassCastException | NullPointerException e) {
                return new CommandResponse(false,
                        Messages.commandError("remove_lower", Messages.INSERT_PARSE_ERROR),
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
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
