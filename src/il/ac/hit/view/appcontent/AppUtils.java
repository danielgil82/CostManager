package il.ac.hit.view.appcontent;

import java.util.Date;

public interface AppUtils {
    void resetUser();
    void changeFrameFromAppViewToLoginView();
    void getCategoriesThatBelongToSpecificUser();
    void getExpensesByCategory(String categoryType);
    void validateAndSetNewCategory(String categoryName);
    void removeCategory(String categoryName);
    void validateAndAddNewCost(String categorySelected, String sumCost, String currency, String description, Date date);
    void removeCost(int categoryID);
    void getCostsID();
    void removeCostsThatReferToChosenCategory(String category);
}
