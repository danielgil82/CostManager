package il.ac.hit.view;

import il.ac.hit.model.User;

/**
 * this interface has the methods that will be invoked from LoginPanel and SignUpPanel
 */
public interface LoginUtils {
   void validateUserExistence(String fullName, String password);
   void addNewUser(User user);
   void validateUsersFullNameAndPasswords(String fullName, String password, String confirmedPassword);
   void validateUserCredentials(String fullName, String password);
}
