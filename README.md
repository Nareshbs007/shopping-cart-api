# Shopping Cart API

A RESTful API for managing shopping carts built with Spring Boot.

## Features

- Create, read, update, and delete shopping carts
- Add and remove items from carts
- Calculate cart totals
- Track cart status (ACTIVE, CHECKOUT, ABANDONED)
- Swagger UI documentation
- H2 in-memory database for development
- Exception handling with detailed error messages

## Tech Stack

- Java 17
- Spring Boot 3.3.0-M2
- Spring Data JPA
- H2 Database
- Maven
- Swagger/OpenAPI 3.0
- Lombok

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Nareshbs007/shopping-cart-api.git
   cd shopping-cart-api
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui.html
```

### Available Endpoints

#### Shopping Cart Operations
- `POST /api/carts` - Create a new shopping cart
- `GET /api/carts/{cartId}` - Get cart details
- `PUT /api/carts/{cartId}` - Update cart status
- `DELETE /api/carts/{cartId}` - Delete a cart

#### Cart Item Operations
- `POST /api/carts/{cartId}/items` - Add item to cart
- `PUT /api/carts/{cartId}/items/{itemId}` - Update item quantity
- `DELETE /api/carts/{cartId}/items/{itemId}` - Remove item from cart

## Database

The application uses H2 in-memory database for development. The database console is available at:
```
http://localhost:8080/h2-console
```

Default credentials:
- JDBC URL: `jdbc:h2:mem:shoppingcartdb`
- Username: `sa`
- Password: `password`

## Error Handling

The API provides detailed error messages for common scenarios:
- Resource not found (404)
- Bad request (400)
- Internal server error (500)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 