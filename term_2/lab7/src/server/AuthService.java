package server;

import java.sql.SQLException;

import database.UserRepository;
import exceptions.ErrorMessages;
import network.AuthData;
import util.PasswordHasher;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String validate(AuthData authData) throws SQLException {
        if (authData == null) {
            return ErrorMessages.AUTH_DATA_MISSING;
        }

        String username = authData.getUsername();
        String password = authData.getPassword();

        if (username == null || username.isBlank()) {
            return ErrorMessages.AUTH_USERNAME_EMPTY;
        }
        
        if (password == null || password.isBlank()) {
            return ErrorMessages.AUTH_PASSWORD_EMPTY;
        }

        String passwordHash = PasswordHasher.sha1(password);

        if (!userRepository.isValidUser(username, passwordHash)) {
            return ErrorMessages.AUTH_INVALID;
        }

        return null;
    }


}