package server.commands;

import java.sql.SQLException;

import database.UserRepository;
import exceptions.ErrorMessages;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import util.PasswordHasher;

public class LoginServerCommand implements ServerCommand {
    private final UserRepository userRepository;

    public LoginServerCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            AuthData authData = request.getAuthData();

            if (authData == null) {
                return new CommandResponse(false, ErrorMessages.AUTH_DATA_MISSING, null);
            }

            String username = authData.getUsername();
            String password = authData.getPassword();

            if (username == null || username.isBlank()) {
                return new CommandResponse(false, ErrorMessages.AUTH_USERNAME_EMPTY, null);
            }

            if (password == null || password.isBlank()) {
                return new CommandResponse(false, ErrorMessages.AUTH_PASSWORD_EMPTY, null);
            }

            String passwordHash = PasswordHasher.sha1(password);

            if (!userRepository.isValidUser(username, passwordHash)) {
                return new CommandResponse(false, ErrorMessages.AUTH_INVALID, null);
            }

            return new CommandResponse(true, "Авторизация выполнена успешно.", null);
        } catch (SQLException e) {
            return new CommandResponse(false, ErrorMessages.commandExecutionError(e.getMessage()), null);
        }
    }

    @Override
    public String getDescription() {
        return "выполнить авторизацию пользователя";
    }
}
