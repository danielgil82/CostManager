package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.Expense;
import il.ac.hit.viewmodel.ViewModel;

import java.util.List;

/**
 * View interface is an interface, that has the methods that are going
 * to be implemented by a concrete view object which is in our case would be an object of the ViewManager class.
 */
public interface View
{
    void setIViewModel(ViewModel vm);
    void displayMessageForLoginSection(Message message);
    void displayMessageForAppSection(Message message);
    void displayMessageAndSetTheFlagValidatorForSignUpPanel(Message message, boolean flag);
    void displayMessageAndSetTheFlagValidatorForLoginPanel(Message message, boolean flag);
    void init();
    void start();
    void changeFrameFromLoginViewToAppView();
    void setCategories(List<String> listOfCategories);
    void setExpensesTableByCategoryInAppView(List<Expense> listOfExpenses);
   // void isTheCategoryNameInputValid(boolean isValid);
    void addNewCategoryToComboBox(String category);
    void removeCategoryFromComboBox(String category);
    void setCostsID(List<Integer> costsID);
    void updateCategoriesComboBoxes(String category);
    void removeCostsFromCostIDComboBox(List<Integer> costsIDToRemove);
    void displayPieChart(List<Expense> costsBetweenGivenDates);
}
