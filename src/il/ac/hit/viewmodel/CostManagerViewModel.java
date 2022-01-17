package il.ac.hit.viewmodel;

import il.ac.hit.auxiliary.HandlingMessage;
import il.ac.hit.model.*;
import il.ac.hit.auxiliary.Message;
import il.ac.hit.view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * CostManagerViewModel is the class that mediates between the view and the model parts.
 * In this class we have overridden methods that were declared in the ViewModel interface.
 * A prat of these methods we have a private methods that do some logic.
 */
public class CostManagerViewModel implements ViewModel {

    private View view;
    private Model model;
    private User user;
    private final ExecutorService service;
    private List<String> categoriesOfTheUser = new ArrayList<>();
    private List<Expense> expensesListByCategory = new ArrayList<>();
    private List<Expense> allCosts = new ArrayList<>();
    private List<Expense> costsBetweenChosenDates = new ArrayList<>();
    private Hashtable<String,Float> costsByCategoryHashTable = new Hashtable<>();
    /**
     * Ctor of the CostManagerViewModel, it constructs the number of the thread
     * that are going to be in the thread pool.
     */
    public CostManagerViewModel() {
        this.service = Executors.newFixedThreadPool(3);
    }

    /**
     * Here we ensure that the user exists in the database.
     *
     * @param fullName - of the user.
     * @param password - of the user.
     */
    @Override
    public void validateUserExistence(String fullName, String password) {
        service.submit(() -> {
            try {
                user = model.getUser(fullName, password);

                //Doesn't require a check on the user because if the user exists it will return into the user, and then
                //frames will be changed, else it will be caught by the catch
                SwingUtilities.invokeLater(() -> view.changeFrameFromLoginViewToAppView());

            } catch (CostManagerException ex) {
                //lambda expression because Runnable is a functional interface
                SwingUtilities.invokeLater(() -> view.displayMessageForLoginSection(new Message(ex.getMessage())));
            }
        });
    }

    /**
     * First we check if the user exists, if it does exist, we catch the exception
     * that tells that the user already exists, else we add a new user to the database,
     * and display a feedback message to the user.
     *
     * @param user - to add to the database.
     */
    @Override
    public void addNewUser(User user) {
        service.submit(() -> {

            try {
                model.checkIfTheUserExists(user);

                model.addNewUserToDBAndUpdateTheListOfUsers(user);

                SwingUtilities.invokeLater(() ->
                        view.displayMessageForLoginSection(new Message(HandlingMessage.SIGNED_UP_SUCCESSFULLY.toString())));

            } catch (CostManagerException ex) {
                //lambda expression because Runnable is a functional interface
                SwingUtilities.invokeLater(() -> view.displayMessageForLoginSection(new Message(ex.getMessage())));
            }
        });
    }

    @Override
    public void getCategoriesBySpecificUser() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    categoriesOfTheUser = model.getCategoriesBySpecificUser(user.getUserID());

                    SwingUtilities.invokeLater(() -> view.setCategories(categoriesOfTheUser));

                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> view.displayMessageForLoginSection(new Message(ex.getMessage())));
                }
            }
        });
    }

    @Override
    public void getExpensesBySpecificCategory(String categoryType) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    expensesListByCategory = model.getExpensesByCategory(user.getUserID(), categoryType);

                    SwingUtilities.invokeLater(() ->
                            view.setExpensesTableByCategoryInAppView(expensesListByCategory));


                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> view.displayMessageForLoginSection(new Message(ex.getMessage())));
                }
            }
        });
    }

    /**
     * This method first validates the input of the user, by ensuring that only letters inserted,
     * afterwards checks that category doesn't already exist, if it does then the user gets a feedback respectively
     * message.
     * Lastly if the input is good, and the category doesn't exist , the method sends the category to the database,
     * to be added.
     *
     * @param category to add
     */
    @Override
    public void validateAndAddNewCategory(String category) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Validation
                    if (validateThatUserInputConsistOnlyLetters(category)) {

                        // Check category existence
                        if (!categoriesOfTheUser.contains(category)) {
                            //Add the new category to the database.
                            model.addNewCategory(new Category(category, user.getUserID()));

                            //Add the category to the list of categories.
                            categoriesOfTheUser.add(category);

                            SwingUtilities.invokeLater(() -> {
                                view.displayMessageForAppSection(new Message
                                        (HandlingMessage.NEW_CATEGORY_ADDED_SUCCESSFULLY.toString()));

                                view.addNewCategoryToComboBox(category);
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                view.displayMessageForAppSection(
                                        new Message(HandlingMessage.CATEGORY_ALREADY_EXISTS.toString()));
                            });
                        }

                    } else {
                        SwingUtilities.invokeLater(() -> {
                            view.displayMessageForAppSection(new Message(HandlingMessage.INVALID_STRING_INPUT.toString()));
                        });
                    }
                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> {
                        view.displayMessageForAppSection(new Message(ex.getMessage()));
                    });
                }
            }
        });
    }


    /**
     * This method is responsible for getting back to the view all the costs that we're purchased between the given
     * dates.
     * @param startDate - start date.
     * @param endDate - end date.
     */
    @Override
    public void getCostsBetweenGivenDates(Date startDate, Date endDate) {
        service.submit(new Runnable() {
            @Override
            public void run() {

                //Validation that the dates aren't null
                if (startDate != null && endDate != null) {
                    try {
                        costsBetweenChosenDates = model.getReportByDates(user.getUserID(),
                                new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));

                        setCostsByCategory();

                        SwingUtilities.invokeLater(() -> {
                            view.displayPieChart(costsByCategoryHashTable);
                        });


                    } catch (CostManagerException ex) {
                        //lambda expression because Runnable is a functional interface
                        SwingUtilities.invokeLater(() -> {
                            view.displayMessageForAppSection(new Message(ex.getMessage()));
                        });
                    }
                } else {
                        SwingUtilities.invokeLater(() -> {
                            view.displayMessageForAppSection(new Message(HandlingMessage.EMPTY_FIELDS.toString()));
                        });
                }
            }
        });
    }

    /**
     * Setting the hash table with a category ,and it's total sum of costs.
     */
    private void setCostsByCategory() {
        if (!costsByCategoryHashTable.isEmpty()) {
            costsByCategoryHashTable.clear();
        }

        for (Expense expense : costsBetweenChosenDates) {
            String category = expense.getCategory();
            Float costSum = expense.getCostSum();

            if(costsByCategoryHashTable.containsKey(category)){
                float updatedSum  = costsByCategoryHashTable.get(category) + costSum;
                costsByCategoryHashTable.put(category, updatedSum);
            }else{
                costsByCategoryHashTable.put(category, costSum);
            }
        }
    }


    /**
     * @param categorySelected
     * @param sumCost
     * @param currency
     * @param description
     * @param date
     */
    @Override
    public void validateAndAddNewCost(String categorySelected, String sumCost,
                                      String currency, String description, Date date) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    if (validateIfEmptyOrNull(sumCost, currency, description, date)) {
                        if (validateIfCorrectInputOrNot(sumCost, currency)) {

                            int cost = Integer.parseInt(sumCost);

                            model.addNewCost(new Expense(categorySelected, cost, currency, description,
                                    new java.sql.Date(date.getTime()), user.getUserID()));

                            getCostsID();

                            SwingUtilities.invokeLater(() -> {
                                view.displayMessageForAppSection(new Message(
                                        HandlingMessage.NEW_COST_ADDED_SUCCESSFULLY.toString()));
                            });
                        }

                    } else {
                        SwingUtilities.invokeLater(() -> {
                            view.displayMessageForAppSection(new Message(HandlingMessage.EMPTY_FIELDS.toString()));
                        });
                    }

                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> {
                        view.displayMessageForAppSection(new Message(ex.getMessage()));
                    });
                }
            }
        });
    }

    /**
     *
     */
    @Override
    public void getCostsID() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    if (allCosts.size() != 0) {
                        allCosts.clear();
                    }

                    allCosts = model.getExpensesByCategory(user.getUserID(), "all");

                    List<Integer> costsID = new ArrayList<>();

                    for (Expense cost : allCosts) {
                        costsID.add(cost.getExpenseID());
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.setCostsID(costsID);
                        }
                    });

                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> {
                        view.displayMessageForAppSection(new Message(ex.getMessage()));
                    });
                }
            }
        });
    }

    /**
     * This method validated if one of the given parameters are null or empty.
     * No need to check categorySelected.equals("") because by default there is always an option checked in
     * the combobox.
     *
     * @param sumCost     - how much it cost
     * @param currency    - currency type
     * @param description - description of the cost
     * @param date        - date it took place.
     * @return if any of these parameters are empty.
     */
    private boolean validateIfEmptyOrNull(String sumCost, String currency, String description, Date date) {
        return !sumCost.equals("") &&
                !currency.equals("") &&
                !description.equals("") &&
                date != null;
    }

    /**
     * This method validates that the input for each field in the add cost panel if it's right.
     *
     * @param sumCost  - the cost of the cost
     * @param currency - type of currency
     * @return boolean that indicates if the params are valid according to the test.
     */
    private boolean validateIfCorrectInputOrNot(String sumCost, String currency) {

        if (!validateInput(sumCost, sc -> sc.chars().allMatch(Character::isDigit))) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    view.displayMessageForAppSection(new Message(HandlingMessage.INVALID_SUM_COST.toString()));
                }
            });

            return false;
        }

        if (!validateInput(currency, c -> c.chars().allMatch(Character::isLetter))) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    view.displayMessageForAppSection(new Message(HandlingMessage.INVALID_CURRENCY.toString()));
                }
            });
            return false;
        }

        return true;
    }

    /**
     * @param costID
     */
    @Override
    public void removeCost(int costID) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    model.removeExistingCost(costID);

                    SwingUtilities.invokeLater(() -> {
                        view.displayMessageForAppSection(new Message(HandlingMessage.COST_REMOVED_SUCCESSFULLY.toString()));
                    });
                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> {
                        view.displayMessageForAppSection(new Message(ex.getMessage()));
                    });
                }
            }
        });
    }

    /**
     * @param categoryToRemove
     */
    @Override
    public void removeSpecificCategory(String categoryToRemove) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (categoriesOfTheUser.contains(categoryToRemove)) {

//                        List<Expense> costsThatRelateToTheChosenCategory = allCosts.stream()
//                                .filter(ex -> ex.getCategory().equals(categoryToRemove)).collect(Collectors.toList());

                        model.removeExistingCategory(new Category(categoryToRemove, user.getUserID()));

                        categoriesOfTheUser.remove(categoryToRemove);


                        SwingUtilities.invokeLater(() -> {
                            view.updateCategoriesComboBoxes(categoryToRemove);

                            view.displayMessageForAppSection(new Message
                                    (HandlingMessage.EXISTING_CATEGORY_REMOVED_SUCCESSFULLY.toString()));
                        });

                    } else {
                        SwingUtilities.invokeLater(() -> {
                            view.displayMessageForAppSection(new Message
                                    (HandlingMessage.PROBLEM_WITH_REMOVING_AN_EXISTING_CATEGORY.toString()));
                        });
                    }

                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> {
                        view.displayMessageForAppSection(new Message(ex.getMessage()));
                    });
                }
            }
        });
    }

    /**
     * @param category
     */
    @Override
    public void removeCostsThatReferToSpecificCategory(String category) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    List<Integer> costToDelete = getCostsThatSupposeToDeleted(category);

                    model.removeCostsBySpecificCategory(new Category(category, user.getUserID()));

                    SwingUtilities.invokeLater(() -> {
                        view.removeCostsFromCostIDComboBox(costToDelete);
                    });


                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> {
                        view.displayMessageForAppSection(new Message(ex.getMessage()));
                    });
                }
            }
        });
    }

    /**
     * This method responsible for create and return list of costs ID's that related to a receiving category.
     *
     * At first this method constructs a list that suppose to have the costsID's that should be
     * deleted from the combo box.
     *
     * Secondly, there is a need to update the list of all costs in this class  "CostManagerViewModel", that's why
     * another data structure was allocated in order to hold the needed expenses that suppose to be deleted.
     *
     * At the end, the expenses that suppose to be deleted get deleted from allCosts list.
     *
     * @param category - the costs that are going to be deleted according to this category.
     * @return list of cost ID's.
     */
    private List<Integer> getCostsThatSupposeToDeleted(String category) {
        List<Integer> costsID = new ArrayList<>();
        List<Expense> toRemove = new ArrayList<>();

        for (Expense expense : allCosts) {
            if (expense.getCategory().equals(category)) {
                costsID.add(expense.getExpenseID());
                toRemove.add(expense);
            }
        }

        toRemove.forEach(ex -> allCosts.remove(ex));

        return costsID;
    }

    /**
     * First we ensure that the awt event thread is the one that run right now.
     * Then we validate users credentials when he signs up.
     *
     * @param fullName          - fullName of the user.
     * @param password          - password of the user.
     * @param confirmedPassword - confirmed password of the user.
     */
    @Override
    public void validateUserCredentialsForSignUpPanel(String fullName, String password, String confirmedPassword) {

        if (SwingUtilities.isEventDispatchThread()) {
            signUpValidatorCredentials(fullName, password, confirmedPassword);
        } else {
            SwingUtilities.invokeLater(() -> signUpValidatorCredentials(fullName, password, confirmedPassword));
        }
    }

    /**
     * First we ensure that the awt event thread is the one that run right now.
     * Then we validate users credentials when he logs in.
     *
     * @param fullName - fullName of the user
     * @param password - password of the user
     */
    @Override
    public void validateUserCredentialsForLoginPanel(String fullName, String password) {
        if (SwingUtilities.isEventDispatchThread()) {
            loginValidatorCredentials(fullName, password);
        } else {
            SwingUtilities.invokeLater(() -> loginValidatorCredentials(fullName, password));
        }
    }

    /**
     * This method suppose to validate the credentials of the user.
     *
     * @param fullName - of the user.
     * @param password - of the user.
     */
    private void loginValidatorCredentials(String fullName, String password) {

        if (fullName.equals("") || password.equals("")) {
            view.displayMessageAndSetTheFlagValidatorForLoginPanel(
                    new Message(HandlingMessage.EMPTY_FIELDS.toString()), false);
        } else if (!validateThatUserInputConsistOnlyLetters(fullName)) {
            view.displayMessageAndSetTheFlagValidatorForLoginPanel
                    (new Message(HandlingMessage.INVALID_FULL_NAME.toString()), false);
        } else {
            view.displayMessageAndSetTheFlagValidatorForLoginPanel
                    (new Message(""), true);
        }
    }

    /**
     * Here we validate users credentials when he signs up. The reason for the validations to appear here, at the
     * view model class is because of a reuse, if in the future we'll change the GUI to another type of GUI
     * this kind of logic would still be in need, that's why it is important to implement it here.
     *
     * @param fullName          - fullName of the user.
     * @param password          - password of the user.
     * @param confirmedPassword - confirmed password of the user.
     */
    private void signUpValidatorCredentials(String fullName, String password, String confirmedPassword) {
        if (fullName.equals("") || password.equals("") || confirmedPassword.equals("")) {
            view.displayMessageAndSetTheFlagValidatorForSignUpPanel(
                    new Message(HandlingMessage.EMPTY_FIELDS.toString()), false);
        } else if (!validateThatUserInputConsistOnlyLetters(fullName)) {
            view.displayMessageAndSetTheFlagValidatorForSignUpPanel
                    (new Message(HandlingMessage.INVALID_FULL_NAME.toString()), false);
        } else if (!confirmPasswords(password, confirmedPassword, String::equals)) {
            view.displayMessageAndSetTheFlagValidatorForSignUpPanel(
                    new Message(HandlingMessage.PASSWORDS_DO_NOT_MATCH.toString()), false);
        } else {
            view.displayMessageAndSetTheFlagValidatorForSignUpPanel(
                    new Message(""), true);
        }
    }

    /**
     * This method use the functional interface as BiPredicate inorder to test if the 2 passwords are equal.
     *
     * @param firstPassword      - first password.
     * @param secondPassword     - second password.
     * @param passwordsMatchTest - functional interface which gets the lambda function and uses the test abstract function.
     * @return true if the passwords match else false.
     */
    private boolean confirmPasswords(String firstPassword, String secondPassword,
                                     BiPredicate<String, String> passwordsMatchTest) {
        return passwordsMatchTest.test(firstPassword, secondPassword);
    }

    /**
     * This method ...
     *
     * @param fullName - users input for full name.
     * @return checks if the full name consists of letters and spaces.
     */
    private boolean validateThatUserInputConsistOnlyLetters(String fullName) {
        boolean isValid = true;

        if (!fullName.equals("")) {
            for (char c : fullName.toCharArray()) {
                if (!Character.isLetter(c) || Character.isWhitespace(c)) {
                    isValid = false;
                    break;
                }
            }
        } else {
            isValid = false;
        }

        return isValid;
    }

    /**
     * This method gets as input a predicate.
     */
    private boolean validateInput(String input, Predicate<String> stringPredicate) {
        return stringPredicate.test(input);
    }

    /**
     * Here we set the view data member.
     *
     * @param view - ..
     */
    @Override
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Here we set the model data member.
     *
     * @param model - the model of our application.
     */
    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Here we reset the user.
     */
    @Override
    public void resetUser() {
        user = null;
    }
}