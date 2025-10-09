Title: Heroku Login – valid credentials

Objective:
- Verify that a user can log in successfully with valid credentials and sees the success flash message.

Preconditions:
- Application is reachable at `https://the-internet.herokuapp.com/login`.
- Test environment uses a stubbed browser client (script generation only) or a real browser if enabled.

Test Data:
- Username: `tomsmith`
- Password: `SuperSecretPassword!`

Steps:
- Navigate to `https://the-internet.herokuapp.com/login`.
- Type the username into `#username`.
- Type the password into `#password`.
- Click the submit button `button[type='submit']`.
- Wait for the flash message `#flash` to become visible.
- Assert that `#flash` text contains `You logged into a secure area!`.
- Capture a screenshot named `heroku-login-success`.

Expected Result:
- The success flash message appears confirming a secure area login.
- (Optional for real browser) The page URL changes to a secure area path and HTTP status is 200.

Traceability:
- Script file: `java/sqm-suite/src/test/java/com/example/sqm/suite/TestHerokuLogin.java`
- Key selectors: `#username`, `#password`, `button[type='submit']`, `#flash`