package il.ac.hit.viewmodel;

import il.ac.hit.model.Model;
import il.ac.hit.model.User;
import il.ac.hit.view.View;

/**
 * This interface has the methods that will be implemented by a concrete ViewModel object
 * and all the methods here are asynchronous because we want the awt event thread to be free
 * when a method from here is invoked. That's why all the methods in ViewModel interface return void.
 */
public interface ViewModel
{
    void setView(View view);
    void setModel(Model model);
    void validateUserExistence(String fullName, String password);
    void addNewUser(User user);
    void getCategoriesBySpecificUser();
    void validateUserCredentialsForSignUpPanel(String fullName, String password, String confirmedPassword);
    void validateUserCredentialsForLoginPanel(String fullName, String password);
    void resetUser();
    void getExpensesBySpecificCategory(String categoryType);
    void addNewCategory(String category);
}
