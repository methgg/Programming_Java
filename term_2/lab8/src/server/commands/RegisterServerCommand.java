package server.commands;

import java.sql.SQLException;

import database.UserRepository;
import exceptions.Messages;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import util.PasswordHasher;

public class RegisterServerCommand implements ServerCommand {
    private final UserRepository userRepository;

    public RegisterServerCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            AuthData authData = request.getAuthData();

            if (authData == null) {
                return new CommandResponse(false, Messages.AUTH_DATA_MISSING, null);
            }

            String username = authData.getUsername();
            String password = authData.getPassword();

            if (username == null || username.isBlank()) {
                return new CommandResponse(false, Messages.AUTH_USERNAME_EMPTY, null);
            }

            if (password == null || password.isBlank()) {
                return new CommandResponse(false, Messages.AUTH_PASSWORD_EMPTY, null);
            }

            if (userRepository.existsByUsername(username)) {
                return new CommandResponse(false, "Пользователь с таким логином уже существует.", null);
            }

            String passwordHash = PasswordHasher.sha1(password);
            userRepository.createUser(username, passwordHash);

            return new CommandResponse(true, "Пользователь успешно зарегистрирован.", null);
        } catch (SQLException e) {
            return new CommandResponse(false, Messages.commandExecutionError(e.getMessage()), null);
        }
    }

    @Override
    public String getDescription() {
        return "зарегистрировать нового пользователя";
    }
}