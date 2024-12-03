Feature: Create and evaluate expressions

    Scenario Outline: Create expression with valid request
        Given I create an expression with name "<name>" and expression "<expression>"
        Then the response should contain a valid expressionId

        Examples:
            | name                | expression                                                                 |
            | Complex expression1 | (customer.firstName == 'John' && customer.salary < 100) OR customer.address.city == 'Washington' |
            | Complex expression2 | (customer.firstName == 'Jane' && customer.salary < 200) OR customer.address.city == 'New York'   |
            | Complex expression3 | (customer.firstName == 'Alice' && customer.salary < 300) OR customer.address.city == 'Los Angeles'|
            | Complex expression4 | (customer.firstName == 'Bob' && customer.salary < 400) AND customer.address.city == 'Chicago'     |
            | Complex expression5 | (customer.firstName == 'Charlie' && customer.salary < 500) AND customer.address.city == 'Houston' |
            | Complex expression6 | (customer.firstName == 'David' && customer.salary < 600) OR customer.address.city == 'Phoenix'   |
            | Complex expression7 | (customer.firstName == 'Eve' && customer.salary < 700) OR customer.address.city == 'Philadelphia'|
            | Complex expression8 | (customer.firstName == 'Frank' && customer.salary < 800) OR customer.address.city == 'San Antonio'|
            | Complex expression9 | (customer.firstName == 'Grace' && customer.salary < 900) OR customer.address.city == 'San Diego' |
            | Complex expression10| (customer.firstName == 'Hank' && customer.salary < 1000) OR customer.address.city == 'Dallas'    |