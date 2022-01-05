package il.ac.hit.view.login;

import il.ac.hit.model.User;

/**
 * this interface has the methods that will be invoked by the okButton and submitButton in
 * LoginPanel and SignUpPanel respectively
 */
public interface LoginUtils {
   void validateUserExistence(String fullName, String password);
   void addNewUser(User user);
   void validateUsersFullNameAndPasswords(String fullName, String password, String confirmedPassword);
   void validateUserCredentials(String fullName, String password);
}