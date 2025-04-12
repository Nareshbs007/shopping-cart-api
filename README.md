# Shopping Cart API

A Spring Boot REST API for managing shopping carts with AWS DynamoDB integration.

## Prerequisites

- Java 17 or higher
- Maven
- AWS Account with DynamoDB access
- AWS CLI configured with appropriate credentials

## Setup

1. Clone the repository
2. Update the AWS credentials in `src/main/resources/application.properties`:
   ```
   aws.access.key.id=your-access-key-id
   aws.secret.access.key=your-secret-access-key
   aws.region=your-aws-region
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Shopping Cart Operations

1. Create a new cart
   ```
   POST /api/carts?userId={userId}
   ```

2. Get cart details
   ```
   GET /api/carts/{cartId}
   ```

3. Add item to cart
   ```
   POST /api/carts/{cartId}/items
   Body:
   {
     "productId": "string",
     "productName": "string",
     "price": number,
     "quantity": number
   }
   ```

4. Remove item from cart
   ```
   DELETE /api/carts/{cartId}/items/{itemId}
   ```

5. Delete cart
   ```
   DELETE /api/carts/{cartId}
   ```

## DynamoDB Tables

The application uses two DynamoDB tables:
1. `ShoppingCarts` - Stores shopping cart information
2. `CartItems` - Stores items in the shopping carts

## Error Handling

The API includes basic error handling for common scenarios:
- Cart not found
- Invalid input data
- AWS service errors

## Security

- AWS credentials should be properly secured and not committed to version control
- Consider using AWS IAM roles and policies for production deployments
- Implement proper authentication and authorization for production use 