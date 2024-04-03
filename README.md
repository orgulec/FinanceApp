
http://localhost:8088/swagger-ui/index.html#

PUT - /account/payment
    {
    "amount": 5000,
    "fromAccount": {"id": 1 },
    "toAccount": {"id": 2 },
    "transactionType": "PAYMENT",
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

GET - /account/1

GET - /account/historyById/1

GET - /account/userAccounts
    {
    "id": 1,
    "firstName": "Janek",
    "lastName": "Kos"
    }

GET - /account/historyByType?id=1&type=DEPOSIT

GET - /transactions/all