package server.commands;

import java.sql.SQLException;

import database.MusicBandRepository;
import exceptions.Messages;
import manager.CollectionManager;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.InsertArgument;
import server.AuthService;


public class InsertServerCommand implements ServerCommand {
    private final CollectionManager cm;
    private final MusicBandRepository musicBandRepository;
    private final AuthService authService;

    public InsertServerCommand(CollectionManager cm, MusicBandRepository musicBandRepository, AuthService authService) {
        this.cm = cm;
        this.musicBandRepository = musicBandRepository;
        this.authService = authService;
    }
    
    @Override
    public CommandResponse execute(CommandRequest request) {
        return cm.withWriteLock(() -> {
            try {
                InsertArgument argument = (InsertArgument) request.getArgument();
                AuthData authData = request.getAuthData();

                Long key = argument.getKey();
                MusicBand band = argument.getMusicBand();
                Long ownerId = authService.requireUserId(authData);

                MusicBand bandToInsert = new MusicBand(
                        band.getName(),
                        band.getCoordinates(),
                        band.getNumberOfParticipants(),
                        band.getGenre(),
                        band.getFrontMan()
                );

                MusicBand persistedBand = musicBandRepository.insert(key, bandToInsert, ownerId);
                cm.insert(key, persistedBand);

                return new CommandResponse(true, Messages.ELEMENT_ADDED, null);
            } catch (ClassCastException | NullPointerException e) {
                return new CommandResponse(false, Messages.commandError("insert", Messages.INSERT_PARSE_ERROR), null);
            } catch (IllegalStateException e) {
                return new CommandResponse(false, e.getMessage(), null);
            } catch (SQLException e) {
                if ("23505".equals(e.getSQLState())) {
                    return new CommandResponse(false, Messages.DUPLICATE_COLLECTION_KEY, null);
                }
                return new CommandResponse(false, Messages.commandExecutionError(e.getMessage()), null);
            }
        });
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент с заданным ключом";
    }
}
