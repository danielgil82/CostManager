package il.ac.hit.viewmodel;

import il.ac.hit.model.Model;
import il.ac.hit.model.User;
import il.ac.hit.view.View;

public interface ViewModel
{
    void setView(View view);
    void setModel(Model model);
    void validateUserExistence(String fullName, String password);
    void addNewUser(User user);
    void getCategoriesBySpecificUser();
    void userCredentialsForSignUpPanel(String fullName, String password, String confirmedPassword);
    void userCredentialsForLoginPanel(String fullName, String password);
}
