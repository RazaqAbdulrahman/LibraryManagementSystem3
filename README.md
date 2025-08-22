# Library API Backend

### How to run
1. Install Java 17+  
2. Install Maven  
3. In the project folder run:

3. Backend will start at: **http://localhost:8080**

### Endpoints
- GET http://localhost:8080/api/books → list all books
- POST http://localhost:8080/api/books → add a book (send JSON)
- POST http://localhost:8080/api/loans/borrow?isbn=...&memberId=...&days=...
- POST http://localhost:8080/api/loans/return?isbn=...&memberId=...
- GET http://localhost:8080/api/members → list members

### Example JSON for adding a book
```json
{
"isbn": "9780132350884",
"title": "Lekan New Book",
"author": "Lekan Razaq",
"totalCopies": 3,
"availableCopies": 3
}
