package server;

import java.sql.SQLException;

import database.UserRepository;
import exceptions.Messages;
import network.AuthData;
import util.PasswordHasher;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String validate(AuthData authData) throws SQLException {
        if (authData == null) {
            return Messages.AUTH_DATA_MISSING;
        }

        String username = authData.getUsername();
        String password = authData.getPassword();

        if (username == null || username.isBlank()) {
            return Messages.AUTH_USERNAME_EMPTY;
        }
        
        if (password == null || password.isBlank()) {
            return Messages.AUTH_PASSWORD_EMPTY;
        }

        String passwordHash = PasswordHasher.sha1(password);

        if (!userRepository.isValidUser(username, passwordHash)) {
            return Messages.AUTH_INVALID;
        }

        return null;
    }


    public Long requireUserId(AuthData authData) throws SQLException {
        if (authData == null) {
            throw new IllegalStateException(Messages.AUTH_DATA_MISSING);
        }

        String username = authData.getUsername();
        if (username == null || username.isBlank()) {
            throw new IllegalStateException(Messages.AUTH_USERNAME_EMPTY);
        }

        Long userId = userRepository.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalStateException(Messages.AUTH_INVALID);
        }

        return userId;
    }

}