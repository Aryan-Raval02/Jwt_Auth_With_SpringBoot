# JWT Authentication with Role-Based Access

<div >
  <h3>Enterprise E-Commerce Authentication System</h3>
  <p>Secure, stateless JWT authentication with role-based access control</p>
  
  ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
  ![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
  ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
  ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
  ![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
</div>

---

## 📋 Project Overview

This project implements a robust authentication and authorization system for an e-commerce platform with the following characteristics:

- **Role-Based Access Control**
  - **Admin:** Full access to manage sellers and system operations
  - **Seller:** Limited access to manage products and own profile
- **Stateless Authentication** using JWT tokens
- **Modular Architecture** designed for scalability

---

## 🛠 Tech Stack

| Technology | Description |
|------------|-------------|
| <img **Spring Boot** | Main framework for REST API development |
| <img **Spring Security** | Authentication and authorization |
| <img **JPA (Hibernate)** | ORM for database persistence |
| <img **MySQL** | Relational database |
| <img **JWT** | Token-based stateless authentication |

---

## 🗄 Database Schema

### User Table
| Column | Type | Description |
|--------|------|-------------|
| **uid** | INT | Primary key |
| **uname** | VARCHAR | Full name of user |
| **username** | VARCHAR | Login username |
| **password** | VARCHAR | Encrypted password |
| **createdAt** | TIMESTAMP | Account creation timestamp |
| **role** | VARCHAR | Role of user (`ADMIN` / `SELLER`) |
| **UUID** | VARCHAR | Encrypted string of `Two fields` |

### Products Table
| Column | Type | Description |
|--------|------|-------------|
| **pid** | INT | Primary key |
| **pname** | VARCHAR | Product name |
| **price** | DECIMAL | Product price |
| **category** | VARCHAR | Product category |
| **description** | TEXT | Product description |
| **inventory** | INT | Available stock |
| **user_uuid** | VARCHAR | Reference to seller (User.UUID) |

---

## ✨ Key Features

- 🔐 **JWT-based stateless authentication** for secure APIs
- 🛡️ **Role-based access control** ensuring proper authorization (ADMIN vs SELLER)
- 🧩 **Modular design** allowing easy addition of modules (Products, Orders, etc.)
- 🏢 **Enterprise-level practices**: JPA, Security filters, token validation, proper database design

---

## 🔌 API Endpoints

### 1. Authentication

| Endpoint | Method | Description | Request Body |
|----------|--------|-------------|--------------|
| `/authenticate` | POST | User login | Required |
| `/logout` | POST | User logout | Not required |

### 2. Admin Operations

| Endpoint | Method | Description | Request Body |
|----------|--------|-------------|--------------|
| `/admin/new` | POST | Create admin | Required |
| `/admin` | PUT | Update admin profile | Required |
| `/admin` | GET | Get details of all users | Not required |
| `/admin/{uuid}` | DELETE | Delete user by ID | Not required |

### 3. Seller Operations

| Endpoint | Method | Description | Request Body |
|----------|--------|-------------|--------------|
| `/seller/new` | POST | Create seller | Required |
| `/seller` | PUT | Update seller profile | Required |
| `/products` | GET | Get all seller's products | Not required |
| `/products/{pid}` | GET | Get single product details | Not required |
| `/products/add` | POST | Create new product | Required |
| `/products/{pid}` | PUT | Update product | Required |
| `/products/{pid}` | DELETE | Delete product | Not required |

---

## 🚀 Getting Started

1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/ecommerce-auth.git
