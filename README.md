# Order Management API Platform

This project implements an **Order Management API Platform** built using Java and Spring Boot. The API provides functionalities for client authentication, order placement, order dispatching, and cancellation, secured with JWT authentication. It also includes a background job that automatically dispatches orders after one hour if they are in a "NEW" state.

## Features

1. **Client Authentication & Authorization**: 
   - Users can sign up and log in to receive a JWT token, which is required to access protected endpoints.
   
2. **Order Management**:
   - Clients can place orders with details such as item name, quantity, and shipping address.
   - Orders can be canceled before dispatching.
   
3. **Order State Management**: 
   - The platform automatically updates the order status from **NEW** to **DISPATCHED** after one hour using a background cron job.
   
4. **Order Status**:
   - NEW
   - DISPATCHED
   - CANCELED
   
5. **Pagination**: Clients can fetch order history with pagination (page number and size).

## Prerequisites

- **Docker**: Make sure Docker is installed. Download from [Docker Official Website](https://www.docker.com/products/docker-desktop).
- **Docker Compose**: Docker Compose should be installed (bundled with Docker Desktop).

## How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/order-management-api.git
cd order-management-api
```
### 2. Build and Run the Application with Docker

```bash
mvn clean install
docker build -t order-management .
docker-compose up
```
