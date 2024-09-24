# Catalog Management System

## Overview

The Catalog Management System is a Spring Boot application that allows users to manage product data effectively. It provides RESTful APIs for performing CRUD (Create, Read, Update, Delete) operations on the Product entity. Users can filter products by various attributes such as name, category, brand, and price range, with support for pagination.

## Features

- **CRUD Operations:** Create, Read, Update, and Delete products.
- **Filtering:** Retrieve products based on name, category, brand, and price range.
- **Pagination:** Support for paginated responses to manage large datasets.
- **Automated Testing:** Includes unit and integration tests written with JUnit and Mockito.

## Technologies Used

- Spring Boot: Version 3.3.4
- Database: MySQL
- Testing Framework: JUnit, Mockito

## Prerequisites

Before running the application locally, ensure you have the following installed:

## Setup Instructions

1. **Create a MySQL Database:**

   - Create a database named `cms` in your MySQL server.

2. **Configure `application.yaml`:**
   - Update the `src/main/resources/application.yaml` file with your MySQL credentials:

```yaml
spring:
  application:
    name: cms
  datasource:
    url: jdbc:mysql://localhost:3306/cms
    username: root
    password: root
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    database: mysql
logging:
  level:
    org:
      springframework: INFO
    com:
      app:
        cms: DEBUG
```

- Ensure to provide your local MySQL server credentials (username and password).

3. Run the Application:

   - Navigate to the main application class, CmsApplication, and run it. The application will start on port 8080.

4. Access the API Documentation:
   - Open your browser and go to http://localhost:8080/swagger-ui.html to view the available APIs and their documentation.

## API Endpoints

1. Get All Products

- URL: /cms/products
- Method: GET
- Query Parameters:
  - pageNumber (optional): The page number to retrieve (default is 0).
  - category (optional): Filter by product category.
  - brand (optional): Filter by product brand.
  - name (optional): Filter by product name.
  - minPrice (optional): Minimum price for filtering.
  - maxPrice (optional): Maximum price for filtering.

2. Get Product by ID

- URL: /cms/product/{id}
- Method: GET
- Path Parameters:
  - id: The ID of the product to retrieve.

3. Create a New Product

- URL: /cms/product
- Method: POST
- Request Body: JSON object containing product details
```
{
    "name": "Product Name",
    "brand": "Brand Name",
    "description": "Product Description",
    "price": 100.0,
    "quantity": 10,
    "category": "Category Name"
}
```

4. Update an Existing Product

- URL: /cms/product
- Method: PUT
- Request Body: JSON object containing updated product details
```
{
    "id": 1,
    "name": "Updated Product Name",
    "brand": "Updated Brand",
    "description": "Updated Description",
    "price": 120.0,
    "quantity": 15,
    "category": "Updated Category"
}
```

5. Delete a Product

- URL: /cms/product/{id}
- Method: DELETE
- Path Parameters:
  - id: The ID of the product to delete.

## Validation Cases

### ProductCreateDto

- Name:

  - Cannot be empty (must be between 2 and 50 characters).

- Brand:

  - Cannot be empty (must be between 2 and 30 characters).

- Description:

  - Cannot be empty (must be between 10 and 200 characters).

- Price:

  - Must be greater than zero and a valid number with up to 2 decimal places.

- Quantity:

  - Cannot be negative (must be between 0 and 1000).

- Category:

  - Cannot be empty (must be between 3 and 30 characters).

### ProductUpdateDto

- ID:

  - Must be provided to identify the product to be updated.

## Testing

The application includes automated tests that can be executed to verify functionality. Use your preferred IDE to run the tests written using JUnit and Mockito.
