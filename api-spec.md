# Shopping Cart API Specification

## Overview
This document describes the REST API for the Shopping Cart service, which allows users to manage their shopping carts and items.

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, the API does not require authentication. For production use, authentication should be implemented.

## API Endpoints

### 1. Create a Shopping Cart
Creates a new shopping cart for a user.

**Request:**
- Method: `POST`
- URL: `/carts`
- Query Parameters:
  - `userId` (required): The ID of the user creating the cart

**Response:**
- Status Code: `200 OK`
- Content-Type: `application/json`
- Body:
  ```json
  {
    "id": "string",
    "userId": "string",
    "items": [],
    "totalAmount": 0.0,
    "status": "ACTIVE"
  }
  ```

### 2. Get Shopping Cart
Retrieves a shopping cart by its ID.

**Request:**
- Method: `GET`
- URL: `/carts/{cartId}`
- Path Parameters:
  - `cartId` (required): The ID of the cart to retrieve

**Response:**
- Status Code: `200 OK`
- Content-Type: `application/json`
- Body:
  ```json
  {
    "id": "string",
    "userId": "string",
    "items": [
      {
        "id": "string",
        "cartId": "string",
        "productId": "string",
        "productName": "string",
        "price": 0.0,
        "quantity": 0
      }
    ],
    "totalAmount": 0.0,
    "status": "ACTIVE"
  }
  ```

**Error Response:**
- Status Code: `404 Not Found` if the cart does not exist

### 3. Add Item to Cart
Adds an item to a shopping cart.

**Request:**
- Method: `POST`
- URL: `/carts/{cartId}/items`
- Path Parameters:
  - `cartId` (required): The ID of the cart to add the item to
- Body:
  ```json
  {
    "productId": "string",
    "productName": "string",
    "price": 0.0,
    "quantity": 0
  }
  ```

**Response:**
- Status Code: `200 OK`
- Content-Type: `application/json`
- Body: Updated cart object with the new item

**Error Response:**
- Status Code: `404 Not Found` if the cart does not exist

### 4. Remove Item from Cart
Removes an item from a shopping cart.

**Request:**
- Method: `DELETE`
- URL: `/carts/{cartId}/items/{itemId}`
- Path Parameters:
  - `cartId` (required): The ID of the cart
  - `itemId` (required): The ID of the item to remove

**Response:**
- Status Code: `200 OK`
- Content-Type: `application/json`
- Body: Updated cart object with the item removed

**Error Response:**
- Status Code: `404 Not Found` if the cart does not exist

### 5. Delete Shopping Cart
Deletes a shopping cart.

**Request:**
- Method: `DELETE`
- URL: `/carts/{cartId}`
- Path Parameters:
  - `cartId` (required): The ID of the cart to delete

**Response:**
- Status Code: `200 OK`

**Error Response:**
- Status Code: `404 Not Found` if the cart does not exist

## Error Handling

All endpoints may return the following error responses:

- `400 Bad Request`: Invalid input data
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server-side error

## Data Models

### ShoppingCart
```json
{
  "id": "string",
  "userId": "string",
  "items": [
    {
      "id": "string",
      "cartId": "string",
      "productId": "string",
      "productName": "string",
      "price": 0.0,
      "quantity": 0
    }
  ],
  "totalAmount": 0.0,
  "status": "string"
}
```

### CartItem
```json
{
  "id": "string",
  "cartId": "string",
  "productId": "string",
  "productName": "string",
  "price": 0.0,
  "quantity": 0
}
``` 