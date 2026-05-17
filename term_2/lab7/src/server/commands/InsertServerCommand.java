package server.commands;

import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

import database.MusicBandRepository;
import database.UserRepository;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.InsertArgument;


public class InsertServerCommand implements ServerCommand {
    private final CollectionManager cm;
    private final MusicBandRepository musicBandRepository;
    private final UserRepository userRepository;

    public InsertServerCommand(CollectionManager cm, MusicBandRepository musicBandRepository, UserRepository userRepository) {
        this.cm = cm;
        this.musicBandRepository = musicBandRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public CommandResponse execute(CommandRequest request) {
        Lock writeLock = cm.getLock().writeLock();
        writeLock.lock();
        try {
            InsertArgument argument = (InsertArgument) request.getArgument();
            AuthData authData = request.getAuthData();

            Long key = argument.getKey();
            MusicBand band = argument.getMusicBand();

            if (authData == null || authData.getUsername() == null || authData.getUsername().isBlank()) {
                return new CommandResponse(false, ErrorMessages.AUTH_DATA_MISSING, null);
            }

            Long ownerId = userRepository.findUserIdByUsername(authData.getUsername());
            if (ownerId == null) {
                return new CommandResponse(false, ErrorMessages.AUTH_INVALID, null);
            }
            
            MusicBand persistedBand = musicBandRepository.insert(key, band, ownerId);
            cm.insert(key, persistedBand);

            return new CommandResponse(true, ErrorMessages.ELEMENT_ADDED, null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false, ErrorMessages.commandError("insert", ErrorMessages.INSERT_PARSE_ERROR), null);
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                return new CommandResponse(false, ErrorMessages.DUPLICATE_COLLECTION_KEY, null);
            }
            return new CommandResponse(false, ErrorMessages.commandExecutionError(e.getMessage()), null);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент с заданным ключом";
    }
}
