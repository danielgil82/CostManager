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
    // view
    private View view;

    //model
    private Model model;

    //logged in user
    private User user;

    // ExecutorService is a thread pool
    private final ExecutorService service;

    // different lists including categories , expensesByCategory , allExpenses, costsBetweenDates
    private List<String> categoriesOfTheUser = new ArrayList<>();
    private List<Expense> expensesListByCategory = new ArrayList<>();
    private List<Expense> allCosts = new ArrayList<>();
    private List<Expense> costsBetweenChosenDates = new ArrayList<>();

    //HashTable that holds categories and their total amount of money spend on.
    private Hashtable<String, Float> costsByCategoryHashTable = new Hashtable<>();

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
     *Adding the user to the database, but first ensuring that he doesn't already exist.
     *
     * @param fullName - user's full name.
     * @param password - user's password.
     */
    @Override
    public void addNewUser(String fullName, String password) {
        service.submit(() -> {

            try {
                //we create a user with the params that came from the view.
                User userToAdd = new User(fullName, password);

                // we check if the user exists if so,an exception will be thrown from the model, and will be caught here.
                model.checkIfTheUserExists(userToAdd);

                //the user will get added to the db
                model.addNewUserToDBAndUpdateTheListOfUsers(userToAdd);

                //save the user here in the view model.
                this.user = userToAdd;

                // let the user know that the new user sign up successfully.
                SwingUtilities.invokeLater(() ->
                        view.displayMessageForLoginSection(new Message
                                (HandlingMessage.SIGNED_UP_SUCCESSFULLY.toString())));


            } catch (CostManagerException ex) {
                //lambda expression because Runnable is a functional interface
                SwingUtilities.invokeLater(() -> view.displayMessageForLoginSection(new Message(ex.getMessage())));
            }
        });
    }


    /**
     * getting a list of categories that belong to the loggedIn user.
     */
    @Override
    public void getCategoriesBySpecificUser() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //first get all the categories of the user from the db.
                    categoriesOfTheUser = model.getCategoriesNamesBySpecificUser(user.getUserID());

                    //send all the categories to the view.
                    SwingUtilities.invokeLater(() -> view.setCategories(categoriesOfTheUser));

                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> view.displayMessageForLoginSection(new Message(ex.getMessage())));
                }
            }
        });
    }

    /**
     * getting back all costs by given category.
     *
     * @param categoryType - the costs will be filtered according to the category.
     */
    @Override
    public void getExpensesBySpecificCategory(String categoryType) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //first get all the expenses from the db according to the category.
                    expensesListByCategory = model.getExpensesByCategory(user.getUserID(), categoryType);

                    //if everything went alright we send the expenses back to the view, to construct the expenses table.
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

                            // if everything is alright, we let the user know that the category was added ,
                            // and we update the combo boxes accordingly.
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
     *
     * @param startDate - start date.
     * @param endDate   - end date.
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

                        // calling this method construct the hash table
                        setCostsByCategory();

                        //if everything is alright we first set the pie chart in the view, and then we set the
                        // costs' table in the view.
                        SwingUtilities.invokeLater(() -> {
                            view.setPieChart(costsByCategoryHashTable);
                            view.setCostsTableInReportPanel(costsBetweenChosenDates);
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
     * it's an auxiliary method that constructs and hashtable for a pie chart usage.
     */
    private void setCostsByCategory() {
       //if the hashtable is not empty, we clear it.
        if (!costsByCategoryHashTable.isEmpty()) {
            costsByCategoryHashTable.clear();
        }

        // construct the hashtable.
        for (Expense expense : costsBetweenChosenDates) {
            String category = expense.getCategory();
            Float costSum = expense.getCostSum();

            // if the hashtable contains the key we just add the sum to value.
            if (costsByCategoryHashTable.containsKey(category)) {
                float updatedSum = costsByCategoryHashTable.get(category) + costSum;
                costsByCategoryHashTable.put(category, updatedSum);
            }
            //else we create the key and then add the sum to this key.
            else {
                costsByCategoryHashTable.put(category, costSum);
            }
        }
    }


    /**
     * Validating the inputs of the user, and adding a new cost.
     *
     * @param categorySelected - the category the user chose.
     * @param sumCost - the amount of money the user spent on that cost.
     * @param currency - the currency he used.
     * @param description - an explanation of the cost.
     * @param date - when it took place.
     */
    @Override
    public void validateAndAddNewCost(String categorySelected, String sumCost,
                                      String currency, String description, Date date) {

        /*
         * first we validate if the one of the fields are empty or null, if so we
         * let the user know that he has empty fields.
         * then we validate if the input corresponds with what it supposes to be.
         *
         */

        service.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    if (validateIfEmptyOrNull(categorySelected, sumCost, currency, description, date)) {

                        if (validateIfCorrectInputOrNot(sumCost, currency)) {

                            //there is no need for try & catch block since we already validated it before.
                            int cost = Integer.parseInt(sumCost);

                            // if every thing is okay we send the cost to the model to get added to the db
                            model.addNewCost(new Expense(categorySelected, cost, currency, description,
                                    new java.sql.Date(date.getTime()), user.getUserID()));

                            //if everything was alright we want to the add the id that was generated in the db to the
                            // combo boxes ,so we call getCostsID that supposed to get all the id's once again
                            //and send them back to the view.
                            getCostsID();

                            // let the user know that everything was okay.
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
     * Getting all the cost id's of the loggedIn user.
     */
    @Override
    public void getCostsID() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    // if the list is empty we clear it.
                    if (allCosts.size() != 0) {
                        allCosts.clear();
                    }

                    //getting back all the costs from the db
                    allCosts = model.getExpensesByCategory(user.getUserID(), "all");

                    // constructing a list of Integers and adding to it all the cost id's that returned from the db.
                    List<Integer> costsID = new ArrayList<>();
                    for (Expense cost : allCosts) {
                        costsID.add(cost.getExpenseID());
                    }

                    //return the cost id's to the view.
                    SwingUtilities.invokeLater(() -> view.setCostsID(costsID));

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
    private boolean validateIfEmptyOrNull(String category, String sumCost, String currency, String description, Date date) {
        return !category.equals("") &&
                !sumCost.equals("") &&
                !currency.equals("") &&
                !description.equals("") &&
                date != null;
    }

    /**
     * This method validates that the input for each field in the add cost panel if it's right.
     *
     * @param sumCost  - the cost of the cost
     * @param currency - type of currency
     *
     * @return boolean that indicates if the params are valid according to the test.
     */
    private boolean validateIfCorrectInputOrNot(String sumCost, String currency) {
        /*
         * sending a predicate to the validateInput and testing it there,
         * ensuring that the sumCost is built only out of Digits.
         */
        if (!validateInput(sumCost, sc -> sc.chars().allMatch(Character::isDigit))) {
            SwingUtilities.invokeLater(() -> view.displayMessageForAppSection(
                    new Message(HandlingMessage.INVALID_SUM_COST.toString())));

            return false;
        }

        /*
         * sending a predicate to the validateInput and testing it there,
         * ensuring that the currency is built only out of letters.
         */

        if (!validateInput(currency, c -> c.chars().allMatch(Character::isLetter))) {
            SwingUtilities.invokeLater(() -> view.displayMessageForAppSection(
                    new Message(HandlingMessage.INVALID_CURRENCY.toString())));

            return false;
        }

        return true;
    }

    /**
     * removing a cost.
     *
     * @param costID - the id of the cost that supposed to be removed.
     */
    @Override
    public void removeCost(int costID) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    // sending the id of the cost that supposed to be removed to the db
                    model.removeExistingCost(costID);

                    // let the user know that the cost removal went successfully.
                    SwingUtilities.invokeLater(() -> {
                        view.displayMessageForAppSection(
                                new Message(HandlingMessage.COST_REMOVED_SUCCESSFULLY.toString()));
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
     * removing a specific category.
     *
     * @param categoryToRemove - the category that the db supposed to remove.
     */
    @Override
    public void removeSpecificCategory(String categoryToRemove) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    //first we ensure that the category indeed exists
                    if (categoriesOfTheUser.contains(categoryToRemove)) {

                        //if it does exist we send it to the model to get removed from the db.
                        model.removeExistingCategory(new Category(categoryToRemove, user.getUserID()));

                        //Also, we remove it from the list of categories we have here.
                        categoriesOfTheUser.remove(categoryToRemove);

                        // if everything went smooth we let the user know that the category was successfully removed,
                        // and we update the combo boxes accordingly
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
     * removing costs that refer to the given category.
     *
     * @param category - the costs are going to be removed by the given category.
     */
    @Override
    public void removeCostsThatReferToSpecificCategory(String category) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    // first we retrieve the cost id's that supposed to be removed.
                    List<Integer> costToDelete = getCostsThatSupposeToDeleted(category);

                    // we send the db the category that it's costs supposed to be removed.
                    model.removeCostsBySpecificCategory(new Category(category, user.getUserID()));


                    // if everything went smooth we update the combo boxes.
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
     * This method responsible for creating and returning list of costs ID's that relate to the given category.
     * At first this method constructs a list that suppose to have the costsID's that should be
     * deleted from the combo box.
     * Secondly, there is a need to update the list of all costs in this class  "CostManagerViewModel", that's why
     * another data structure was allocated in order to hold the needed expenses that suppose to be deleted.
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

        // removing the needed costs from all costs
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
     * The code in this method is invoked from the method validateUserCredentialsForLoginPanel
     * which is invoked within awt event thread.
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
     * The code in this method is invoked from the method validateUserCredentialsForSignUpPanel
     * which is invoked within awt event thread.
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
     * This method uses the functional interface as BiPredicate inorder to test if the 2 passwords are equal.
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
     * A validation on the fullName, that it consists only letters and only and nothing else,
     * if it does consist something else we return false.
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
     * This method gets as input a predicate,
     * and tests the input.
     */
    private boolean validateInput(String input, Predicate<String> stringPredicate) {
        return stringPredicate.test(input);
    }

    /**
     * Here we set the view data member.
     *
     * @param view - part of the project.
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