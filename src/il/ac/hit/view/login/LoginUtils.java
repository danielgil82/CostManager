package il.ac.hit.view.login;

import il.ac.hit.model.User;

/**
 * This interface has the methods that will be invoked by the okButton and submitButton in
 * LoginPanel and SignUpPanel respectively
 */
public interface LoginUtils {

   /**
    * This method will be invoked when the buttonOk in the loginPanel will be clicked
    *
    * @param fullName - users name.
    * @param password - users password.
    */
   void validateUserExistence(String fullName, String password);

   /**
    * This method adds sends the fullName and the password to the viewModel.
    *
    * @param fullName - user's full name.
    * @param password - user's password.
    */
   void addNewUser(String fullName, String password);

   /**
    * This method is responsible for moving the fullName and the password to the viewModel.
    *
    * @param fullName - of the user.
    * @param password - password the user choose.
    * @param confirmedPassword - confirmation of the users' password.
    */
   void validateUsersFullNameAndPasswords(String fullName, String password, String confirmedPassword);

   /**
    *  This method suppose to send the full name and the password of the user
    *  to the viewModel, because of a reuse thinking, because if the in the future
    *  we'd change the gui part to another one, the logic of validating users credentials
    *  will still be relevant.
    *
    * @param fullName - of the user.
    * @param password - of the user.
    */
   void validateUserCredentials(String fullName, String password);
}
