
Design task: 
Application for managing personal finances

Objective:
The goal of the project is a REST application that enables users
personal finance management. The application will allow you to track your income,
expenses, budget planning, and financial analysis.

Functionalities:
1. Financial account management:
* Adding different account types (e.g. cash, credit cards, savings).
* Balance update for each account.

2. Recording transactions:
* Adding income and expense transactions, specifying category, amount, date and account.
* Ability to assign transactions to a specific budget.

3. Budget planning:
* Creating monthly budgets specifying spending limits for various categories.
* Blocking transactions if the specified budget is exceeded

4. Financial analysis: (in progress)
* Generating reports and charts showing the structure of expenses, revenues and savings.
* Comparing expenses between periods.

5. Security and access management:
* User registration and login.

<hr>
<b>SWAGGER:</b>

http://localhost:8088/swagger-ui/index.html#

PUT - /account/payment
    {
    "amount": 5000,
    "fromAccount": {"id": 1 },
    "toAccount": {"id": 2 },
    "transactionType": "ENTERTAINMENT",
    "title": "Płatność"
    }

POST - /account/createAccount
    {
    "owner": {"id": 1 },
    "type": "DEBIT",
    "amount": 0,
    "login": "jankos",
    "password": "password"
    }

POST - /account/createDeposit
    {
    "accountId": 1,
    "balance": 2500,
    "plannedEndDate": "2025-03-26T01:02:27.8793178+01:00"
    }

POST - /budget/createBudgetLimit
    {
    "account":{
    "id": 1
    },
    "type": "HEALTH",
    "limit": 6000,
    "title": "Zdrowie"
    }

GET - /account/userAccounts
    {
    "id": 1,
    "firstName": "Janek",
    "lastName": "Kos"
    }

GET - /account/1

GET - /account/historyById/1

GET - /account/historyByType?id=1&type=ENTERTAINMENT

GET - /transactions/all

GET - /budget/allByAccount/1

GET - /budget/statistics/1

<hr>