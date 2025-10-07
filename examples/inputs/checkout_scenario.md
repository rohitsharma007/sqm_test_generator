Title: Checkout flow – add to cart, apply coupon, pay

Preconditions:
- User account exists: demo@example.com / secret
- Product "Wireless Mouse" is in stock
- Coupon code: SAVE10 (10% off)

UI Steps:
1. Open home page
2. Search for "Wireless Mouse" and open product page
3. Click "Add to Cart"
4. Open cart and apply coupon SAVE10
5. Proceed to checkout, fill shipping details
6. Select payment method "Credit Card"
7. Confirm payment and view order confirmation

API Details:
- POST /api/auth/login {"user":"demo@example.com","pass":"secret"}
- GET /api/products?q=wireless%20mouse
- POST /api/cart/add {"sku":"WM-123","qty":1}
- POST /api/cart/apply-coupon {"code":"SAVE10"}
- POST /api/checkout {"payment":"card","address":"123 Main"}

Data Variations:
- env: [dev, qa]
- payment: [card, upi]