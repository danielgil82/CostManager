package il.ac.hit.viewmodel;

import il.ac.hit.model.Model;
import il.ac.hit.model.User;
import il.ac.hit.view.View;

public interface ViewModel
{
    public void setView(View view);
    public void setModel(Model model);
    public void validateUserExistence(String fullName, String password);
    public void addNewUser(User user);
    public void getCategoriesBySpecificUser();
}
