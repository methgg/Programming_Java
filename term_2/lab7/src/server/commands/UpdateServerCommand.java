package server.commands;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import database.MusicBandRepository;
import exceptions.Messages;
import manager.CollectionManager;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.UpdateArgument;
import server.AuthService;


public class UpdateServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final MusicBandRepository musicBandRepository;
    private final AuthService authService;

    public UpdateServerCommand(CollectionManager collectionManager,
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
                UpdateArgument argument = (UpdateArgument) request.getArgument();
                AuthData authData = request.getAuthData();

                Long id = argument.getId();
                MusicBand newBand = argument.getMusicBand();

                Long userId = authService.requireUserId(authData);

                Optional<Map.Entry<Long, MusicBand>> existingEntry = collectionManager.getCollection().entrySet().stream()
                        .filter(entry -> entry.getValue().getId().equals(id))
                        .findFirst();

                if (existingEntry.isEmpty()) {
                    return new CommandResponse(false, Messages.elementWithIdNotFound(id), null);
                }

                if (!musicBandRepository.isOwnerByBandId(id, userId)) {
                    return new CommandResponse(false, Messages.ACCESS_DENIED, null);
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
                            Messages.commandExecutionError("Объект не удалось обновить в базе данных."),
                            null);
                }

                collectionManager.getCollection().put(existingEntry.get().getKey(), bandToPersist);

                return new CommandResponse(true, Messages.updatedById(id), null);
            } catch (ClassCastException | NullPointerException e) {
                return new CommandResponse(false,
                        Messages.commandError("update", Messages.INVALID_ID),
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
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
