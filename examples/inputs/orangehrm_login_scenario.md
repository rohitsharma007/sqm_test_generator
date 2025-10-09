Title: OrangeHRM – login and verify dashboard

Objective:
- Verify that a user can log in with valid credentials and reach the Dashboard page.

Preconditions:
- Application is reachable at `https://opensource-demo.orangehrmlive.com/`.
- Test environment uses a stubbed browser client (script generation only) or a real browser if enabled.

Test Data:
- Username: `Admin`
- Password: `admin123`

Steps:
- Navigate to `https://opensource-demo.orangehrmlive.com/`.
- Type the username into `input[name='username']`.
- Type the password into `input[name='password']`.
- Click the login button `button[type='submit']`.
- Wait for the dashboard header `h6:contains('Dashboard')` or element `[class*='oxd-topbar-header-title']` to become visible.
- Assert that the page shows `Dashboard` in the top header.
- Capture a screenshot named `orangehrm-dashboard`.

Expected Result:
- User lands on the Dashboard page after login and sees a visible `Dashboard` header.

Traceability:
- Script file: `java/sqm-suite/src/test/java/com/example/sqm/suite/TestOrangeHrmLogin.java`
- Key selectors: `input[name='username']`, `input[name='password']`, `button[type='submit']`, `[class*='oxd-topbar-header-title']`