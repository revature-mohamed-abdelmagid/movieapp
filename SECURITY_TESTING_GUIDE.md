# 🔐 Spring Security Testing Guide

This guide will help you test the newly implemented Spring Security authentication and authorization.

## 📋 What Was Implemented

### 1. **Password Encryption**
- All passwords are encrypted using BCrypt before storing in database
- BCrypt automatically handles salt generation

### 2. **Authentication Method**
- **HTTP Basic Authentication** - Simple username:password authentication
- Sent in the `Authorization` header as: `Basic <base64(username:password)>`

### 3. **Authorization (Role-Based Access Control)**

#### Public Endpoints (No Authentication Required):
- `POST /api/auth/register` - Register new user
- `GET /api/auth/login-info` - Get login information
- `GET /movies/**` - View all movies

#### Authenticated Endpoints (Any logged-in user):
- `POST /movies/**` - Create movies
- `PUT /movies/**` - Update movies

#### Admin Only Endpoints:
- `DELETE /movies/**` - Delete movies
- `GET /api/users/**` - View all users
- `POST /api/users/**` - Create users
- `PUT /api/users/**` - Update users
- `DELETE /api/users/**` - Delete users

## 🚀 Testing Steps

### Step 1: Start Your Application

```bash
cd /Users/mohamedabdelmagid/Documents/moviespring/backend
./mvnw spring-boot:run
```

Or run it from your IDE.

### Step 2: Register a Regular User

**Endpoint:** `POST http://localhost:8085/api/auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "message": "User registered successfully",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

**Note:** User is automatically assigned `ROLE_USER` role.

### Step 3: Register an Admin User (via Database)

Since we don't have an admin creation endpoint, you need to manually create an admin user in the database:

**Option A: Using MySQL Workbench or command line:**

```sql
-- First, register a user normally via the API (e.g., username: admin_user)
-- Then update their role in the database:

-- Find the user_id
SELECT * FROM users WHERE username = 'admin_user';

-- Add ROLE_ADMIN to the user_roles table
INSERT INTO user_roles (user_id, role) VALUES (2, 'ROLE_ADMIN');
```

**Option B: Or register and manually add in one go:**

```sql
-- Insert admin user with BCrypt encrypted password "admin123"
INSERT INTO users (username, email, password, createdat) 
VALUES ('admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', UNIX_TIMESTAMP());

-- Add ROLE_ADMIN role (replace 1 with the actual user_id)
INSERT INTO user_roles (user_id, role) VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN');
```

### Step 4: Test Public Endpoints (No Authentication)

#### View Movies (Public)
```http
GET http://localhost:8085/movies
```
✅ Should work without authentication

#### Try to Create Movie (Should Fail)
```http
POST http://localhost:8085/movies
Content-Type: application/json

{
  "title": "Inception",
  "genre": "Sci-Fi"
}
```
❌ Should return `401 Unauthorized`

### Step 5: Test Authenticated Endpoints

#### Login and Create Movie (Regular User)

**Using Postman:**
1. Go to the **Authorization** tab
2. Select **Basic Auth**
3. Enter username: `john_doe`
4. Enter password: `password123`
5. Send the request:

```http
POST http://localhost:8085/movies
Content-Type: application/json

{
  "title": "Inception",
  "genre": "Sci-Fi",
  "releaseYear": 2010
}
```
✅ Should work - returns created movie

#### Try to Delete Movie (Should Fail for Regular User)
```http
DELETE http://localhost:8085/movies/1
Authorization: Basic am9obl9kb2U6cGFzc3dvcmQxMjM=
```
❌ Should return `403 Forbidden` (user doesn't have ADMIN role)

### Step 6: Test Admin Endpoints

#### Login as Admin and Delete Movie

**Using Postman:**
1. Change Authorization credentials to admin user
2. Username: `admin`
3. Password: `admin123` (or whatever password you set)
4. Send request:

```http
DELETE http://localhost:8085/movies/1
```
✅ Should work - returns `204 No Content`

#### View All Users (Admin Only)
```http
GET http://localhost:8085/api/users
Authorization: Basic <admin credentials>
```
✅ Should work - returns list of users

## 🧪 Testing with cURL

If you prefer command line testing:

### Register User
```bash
curl -X POST http://localhost:8085/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "test123"
  }'
```

### View Movies (Public)
```bash
curl http://localhost:8085/movies
```

### Create Movie (Authenticated)
```bash
curl -X POST http://localhost:8085/movies \
  -H "Content-Type: application/json" \
  -u testuser:test123 \
  -d '{
    "title": "The Matrix",
    "genre": "Sci-Fi",
    "releaseYear": 1999
  }'
```

### Delete Movie (Admin Only)
```bash
curl -X DELETE http://localhost:8085/movies/1 \
  -u admin:admin123
```

## 🔍 Checking Database

After registration, check your database:

```sql
-- View all users
SELECT * FROM users;

-- View user roles
SELECT u.username, ur.role 
FROM users u 
LEFT JOIN user_roles ur ON u.id = ur.user_id;
```

You should see:
- Passwords are encrypted (starting with `$2a$`)
- Users have their roles in the `user_roles` table

## ⚠️ Common Issues

### 1. 401 Unauthorized
- Check username and password are correct
- Make sure you're sending the Authorization header

### 2. 403 Forbidden
- User is authenticated but doesn't have the required role
- Check user roles in database

### 3. Password Not Encrypted
- Make sure you're registering via `/api/auth/register`
- Don't create users directly via `/api/users` endpoint (use admin registration instead)

## 🎯 Next Steps

This is a **basic implementation** for testing. For production, consider:

1. **JWT Tokens** instead of Basic Auth
2. **Refresh Tokens** for better security
3. **Email Verification** for registration
4. **Password Reset** functionality
5. **Rate Limiting** on login attempts
6. **CORS Configuration** for frontend
7. **Admin Creation Endpoint** with proper authorization

## 📝 Quick Reference

| Endpoint | Method | Authentication | Authorization |
|----------|--------|----------------|---------------|
| `/api/auth/register` | POST | None | Public |
| `/api/auth/login-info` | GET | None | Public |
| `/movies` | GET | None | Public |
| `/movies` | POST | Required | Any User |
| `/movies/{id}` | PUT | Required | Any User |
| `/movies/{id}` | DELETE | Required | Admin Only |
| `/api/users/**` | ALL | Required | Admin Only |

---

Happy Testing! 🚀

