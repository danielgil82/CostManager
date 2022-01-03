package il.ac.hit.auxiliary;

/**
 * This Enum has all the messages that we use in our application.
 */
public enum HandlingMessage
{
   DUPLICATED_ROW("Oops seems like this row is already defined"),
   USER_DOES_NOT_EXISTS ("User doesn't exist, try again or sign up"),
   USER_ALREADY_EXISTS ( "User already exists, sign up with different details"),
   SIGNED_UP_SUCCESSFULLY ("You signed up successfully"),
   PROBLEM_WITH_REGISTERING_THE_DRIVER("Problem with registering driver to the driver manager"),
   COULD_NOT_GET_THE_CATEGORIES ("Couldn't get the categories"),
   PROBLEM_WITH_THE_CONNECTION ("problems with the connection"),
   PROBLEM_WITH_CLOSING_THE_CONNECTION ("problems with closing the connection"),
   PROBLEM_WITH_THE_RESULT_SET("problems with the result set"),
   PROBLEM_WITH_THE_STATEMENT ("problems with the prepared statement"),
   PROBLEM_WITH_ROLLING_BACK ("problem with rolling back"),
   PROBLEM_WITH_REMOVING_AN_EXISTING_CATEGORY("problem with removing and existing category"),
   PROBLEM_WITH_REMOVING_SPECIFIC_EXPENSES_BY_SPECIFIC_CATEGORY ("problem with removing expenses by specific category."),
   COULD_NOT_GET_ALL_EXPENSES ("Couldn't get all expenses"),
   CATEGORY_ALREADY_EXISTS ("The category already exists, try another one"),
   EXPENSE_ALREADY_EXISTS ("Oops seems like this expense already exists, try another one"),
   PROBLEM_WITH_GETTING_THE_USERS ("problems with getting the users"),
   PROBLEM_WITH_ADDING_NEW_USER ("problems with adding a new user"),
   PROBLEM_WITH_ADDING_NEW_CATEGORY ("problem with adding new category"),
   PROBLEM_WITH_ADDING_NEW_EXPENSE ("problem with adding new expense"),
   PROBLEM_WITH_REMOVING_EXISTING_EXPENSE ("problem with removing an existing expense"),
   COULD_NOT_FIND_EXPENSES_BETWEEN_THESE_DATES ("Couldn't find any expenses between these two dates"),
   SOMETHING_WENT_WRONG ("Something went wrong"),
   EMPTY_FIELDS ("One or more of the fields are empty"),
   INVALID_FULL_NAME ( "Invalid full name, please try again"),
   PASSWORDS_DO_NOT_MATCH ( "The passwords you entered don't match");

   private String message;

   HandlingMessage(String message) {
      this.message = message;
   }

   public String getMessage() {
      return message;
   }

   @Override
   public String toString() {
      return message;
   }
}
