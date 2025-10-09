Title: Bing Search – find and open first result

Objective:
- Validate that a user can perform a search on Bing and open the first organic result.

Preconditions:
- Application is reachable at `https://www.bing.com`.
- Test environment uses a stubbed browser client (script generation only) or a real browser if enabled.

Test Data:
- Query: `Northern Trust`

Steps:
- Navigate to `https://www.bing.com`.
- Type the query into the search input `#sb_form_q`.
- Press Enter on `#sb_form_q` to submit.
- Wait for results `//ol[@id='b_results']//li[contains(@class,'b_algo')]//h2` to become visible.
- Click the first result link using `ancestor::a` from the first `h2`.

Expected Result:
- The first organic result opens in the browser.
- (Optional for real browser) The destination page loads with HTTP 200 and relevant title.

Traceability:
- Script file: `java/sqm-suite/src/test/java/com/example/sqm/suite/TestGoogleSearch.java`
- Key selectors: `#sb_form_q`, `//ol[@id='b_results']//li[contains(@class,'b_algo')]//h2`, `ancestor::a`