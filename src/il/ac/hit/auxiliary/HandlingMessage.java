package il.ac.hit.auxiliary;

/**
 * This Enum has all the messages that we use in our application.
 */
public enum HandlingMessage
{
   /**
    *
    */
   DUPLICATED_ROW("Oops seems like this row is already defined"),

   /**
    * Handling messages that relate to user.
    */
   USER_DOES_NOT_EXISTS ("User doesn't exist, try again or sign up"),
   USER_ALREADY_EXISTS ( "User already exists, sign up with different details"),
   PROBLEM_WITH_GETTING_THE_USERS ("problems with getting the users"),
   PROBLEM_WITH_ADDING_NEW_USER ("problems with adding a new user"),
   INVALID_FULL_NAME ( "Invalid full name, please try again"),
   PASSWORDS_DO_NOT_MATCH ( "The passwords you entered don't match"),
   SIGNED_UP_SUCCESSFULLY ("You signed up successfully"),


   /**
    * handling messages that relate to database
    */
   PROBLEM_WITH_THE_CONNECTION ("problems with the connection"),
   PROBLEM_WITH_CLOSING_THE_CONNECTION ("problems with closing the connection"),
   PROBLEM_WITH_REGISTERING_THE_DRIVER("Problem with registering driver to the driver manager"),
   PROBLEM_WITH_THE_RESULT_SET("problems with the result set"),
   PROBLEM_WITH_THE_STATEMENT ("problems with the prepared statement"),
   PROBLEM_WITH_ROLLING_BACK ("problem with rolling back"),

   /**
    * handling messages that relate to the categories table
    */
   NEW_CATEGORY_ADDED_SUCCESSFULLY("The Category added successfully"),
   CATEGORY_ALREADY_EXISTS ("The category already exists, try another one"),
   COULD_NOT_GET_THE_CATEGORIES ("Couldn't get the categories"),
   PROBLEM_WITH_REMOVING_AN_EXISTING_CATEGORY("problem with removing and existing category"),
   PROBLEM_WITH_ADDING_NEW_CATEGORY ("problem with adding new category"),
   EXISTING_CATEGORY_REMOVED_SUCCESSFULLY("The category removed successfully"),


   /**
    * handling messages that relate to the costs table.
    */
   PROBLEM_WITH_REMOVING_SPECIFIC_EXPENSES_BY_SPECIFIC_CATEGORY ("problem with removing expenses by specific category."),
   COULD_NOT_GET_ALL_EXPENSES ("Couldn't get all expenses"),
   EXPENSE_ALREADY_EXISTS ("Oops seems like this expense already exists, try another one"),
   PROBLEM_WITH_ADDING_NEW_EXPENSE ("problem with adding new expense"),
   PROBLEM_WITH_REMOVING_EXISTING_EXPENSE ("problem with removing an existing expense"),
   COULD_NOT_FIND_EXPENSES_BETWEEN_THESE_DATES ("Couldn't find any expenses between these two dates"),

   /**
    * handling messages that relate to general usage
    */
   SOMETHING_WENT_WRONG ("Something went wrong"),
   EMPTY_FIELDS ("One or more of the fields are empty"),
   INVALID_STRING_INPUT("Please use only letters"),
   INVALID_INTEGER_INPUT("Please use only digits");


   /**
    * message that represents the handling message
    */
   private String message;

   /**
    * ctor that receives the message
    * @param message the message we received
    */
   HandlingMessage(String message) {
      this.message = message;
   }

   /**
    * getter that return the message
    * @return the message
    */
   public String getMessage() {
      return message;
   }

   /**
    * Override to the toString.
    * @return message.
    */
   @Override
   public String toString() {
      return message;
   }
}