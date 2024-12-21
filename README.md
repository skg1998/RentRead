# RentRead
> Develop a RESTful API service using Spring Boot to manage an online book rental system while using MySQL to persist the data.

## Database Design
![Untitled](https://github.com/user-attachments/assets/6e9b83f8-5c44-43de-8008-225fa74a68ac)

## Endpoints

### User
- GET /user - Retrieve a list of all registered user
- GET /user/{userId} - Retrieve the details of a specific user
- POST /user - Register a new user
- PUT /user/{userId} - Update detail of a specific user
- DELETE /user/{userId} - Delete a specific user

### Book
- GET /book - Retrieve a list of all created book
- GET /book/{bookId} - Retrieve the details of a specific book
- Get /book/avalibility/{bookId} - check book avaliable or not
- POST /book - Create a new book
- PUT /book/{bookId} - Update detail of a specific book
- DELETE /book/{bookId} - Delete a specific book

### Rent
- GET /rent/book/{bookId} - rent a book
- GET /rent/book/return/{bookId} - return a book

## Usage
> this application is implemented on the basis of Basic Auth, so you have to path email and passowrd with all secure request. Also implemented Authrization (ADMIN, USER)

## Postman Collection
[Rental Management.postman_collection.json](https://github.com/user-attachments/files/18219216/Rental.Management.postman_collection.json)
