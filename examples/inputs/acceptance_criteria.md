Feature: User login

Scenario: Successful login with valid credentials
Given the user is on the login page
When they enter valid username and password
Then they see the dashboard and a welcome message

API: POST /auth/login returns 200 and JSON { token }