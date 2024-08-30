package app.financeapp.utils.enums;

public enum ExceptionMsg {
    NO_TRANSACTIONS_FOUNDED("No transaction founded!"),
    INCORRECT_AMOUNT_VALUE("Incorrect amount value!"),
    INSUFFICIENT_FUNDS_IN_ACCOUNT("Insufficient funds in the account!"),
    CHECK_ACCOUNTS_DATA("Check accounts data!"),
    BUDGET_LIMIT_NOT_FOUND("Budget limit not found!"),
    BUDGET_LIMIT_ALREADY_EXIST("Budget limit already exist!"),
    NO_BUDGET_LIMIT_FOUNDED("No budget limit founded!"),
    NO_ACCOUNT_FOUNDED("No account founded!"),
    AMOUNT_EXCEED_THE_BUDGET_LIMIT("The amount exceeded the budget limit!"),
    USER_NOT_FOUND("User not found!");

    ExceptionMsg(String s) {
    }


}
