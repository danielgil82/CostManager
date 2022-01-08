package il.ac.hit.view.appcontent;

public interface AppUtils {
    void resetUser();
    void changeFrameFromAppViewToLoginView();
    void getCategoriesThatBelongToSpecificUser();
    void getExpensesByCategory(String categoryType);
    void validateAndSetNewCategory(String categoryName);
    void removeCategory(String categoryName);
}
