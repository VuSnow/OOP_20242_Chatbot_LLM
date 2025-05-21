package service.user;

import java.sql.*;
import java.util.regex.Pattern;

import common.exception.*;
import entity.user.User;
import entity.user.UserDAO;

public class UserService {
    private final UserDAO userDAO;
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User authenticate(String username, String password) throws SQLException {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username must not be empty");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password must not be empty");

        return this.userDAO.findByUsernameAndPassword(username, password);
    }

    public void validateForSignup(User user, String confirmPassword)
            throws PasswordMismatchException, InvalidEmailFormatException, UsernameAlreadyExistsException, SQLException {

        if (!user.getPassword().equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        if (!EMAIL_REGEX.matcher(user.getEmail()).matches()) {
            throw new InvalidEmailFormatException();
        }
        if (userDAO.findByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
    }

    public boolean registerUser(User user, String confirmPassword)
            throws PasswordMismatchException, InvalidEmailFormatException, UsernameAlreadyExistsException, SQLException {
        validateForSignup(user, confirmPassword);
        return userDAO.insert(user);
    }
}
