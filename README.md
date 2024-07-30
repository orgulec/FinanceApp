### Design task:
Application for managing personal finances
<hr>

### Objective:
The goal of the project is a REST application that enables users
personal finance management. The application will allow you to track your income,
expenses, budget planning, and financial analysis.
<hr>

### Functionalities:

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

<url>http://localhost:8088/swagger-ui/index.html#</url>

### Basic Auth:
Login - <i>AccountUser</i>

Password - <i>pass</i>

PUT - /account/payment
<code>{
"amount": 5000,
"fromAccount": {"id": 1 },
"toAccount": {"id": 2 },
"transactionType": "ENTERTAINMENT",
"title": "Płatność"
}</code>

POST - /account/newAccount
<code>{
"owner": {"id": 1 },
"type": "DEBIT",
"amount": 0,
"login": "jankos",
"password": "password"
}</code>

POST - /account/newDeposit
<code>{
"accountId": 1,
"balance": 2500,
"plannedEndDate": "2025-03-26T01:02:27.8793178+01:00"
}</code>

POST - /budget/newBudgetLimit
<code>{
"account":{
"id": 1
},
"type": "HEALTH",
"limit": 6000,
"title": "Zdrowie"
}</code>

GET - /account/userAccounts
<code>{
"id": 1,
"firstName": "Janek",
"lastName": "Kos"
}</code>

GET - /account/1

GET - /account/history/1

GET - /account/history?id=1&type=ENTERTAINMENT

GET - /transactions/all

GET - /budget/byAccount/1

GET - /budget/statistics/1

<hr>
