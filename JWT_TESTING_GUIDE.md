# 🔐 JWT Authentication Testing Guide

## ✅ What Was Implemented

You now have **JWT (JSON Web Token) authentication** with proper logout functionality!

### Key Features:
- ✅ JWT token-based authentication
- ✅ Token expiration (1 hour by default)
- ✅ Proper logout with token blacklisting
- ✅ Stateless authentication (scalable)
- ✅ Password encryption with BCrypt
- ✅ Role-based authorization

---

## 🚀 Quick Start Testing

### Step 1: Start Your Application

```bash
cd /Users/mohamedabdelmagid/Documents/moviespring/backend
./mvnw clean install
./mvnw spring-boot:run
```

Wait for "Started MovieappApplication" message.

---

### Step 2: Register a User

**Endpoint:** `POST http://localhost:8085/api/auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8085/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123"
  }'
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

---

### Step 3: Login and Get JWT Token

**Endpoint:** `POST http://localhost:8085/api/auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTY5ODc2NTQzMiwiZXhwIjoxNjk4NzY5MDMyLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dfQ.xyz123...",
  "type": "Bearer",
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

**💡 Important:** Copy the `token` value - you'll need it for authenticated requests!

---

### Step 4: Use Token to Access Protected Endpoints

Now use the JWT token in the `Authorization` header:

**Format:** `Authorization: Bearer YOUR_TOKEN_HERE`

#### Example: Create a Movie

**Endpoint:** `POST http://localhost:8085/movies`

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Inception",
  "genre": "Sci-Fi",
  "releaseYear": 2010
}
```

**cURL:**
```bash
curl -X POST http://localhost:8085/movies \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Inception",
    "genre": "Sci-Fi",
    "releaseYear": 2010
  }'
```

✅ Should successfully create the movie!

---

### Step 5: Test Logout

**Endpoint:** `POST http://localhost:8085/api/auth/logout`

**Headers:**
```
Authorization: Bearer YOUR_TOKEN_HERE
```

**cURL:**
```bash
curl -X POST http://localhost:8085/api/auth/logout \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Expected Response:**
```json
{
  "message": "Logged out successfully"
}
```

---

### Step 6: Verify Token is Blacklisted

Try to use the same token again:

```bash
curl -X POST http://localhost:8085/movies \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_OLD_TOKEN" \
  -d '{
    "title": "Another Movie",
    "genre": "Action"
  }'
```

❌ Should fail - token is now invalid (blacklisted)

---

## 📱 Testing with Postman

### 1. Register User

1. Create new request
2. Method: **POST**
3. URL: `http://localhost:8085/api/auth/register`
4. Go to **Body** tab → **raw** → **JSON**
5. Add:
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "test123"
}
```
6. Click **Send**

### 2. Login (Get Token)

1. Create new request
2. Method: **POST**
3. URL: `http://localhost:8085/api/auth/login`
4. Go to **Body** tab → **raw** → **JSON**
5. Add:
```json
{
  "username": "testuser",
  "password": "test123"
}
```
6. Click **Send**
7. **Copy the token from response!**

### 3. Use Token for Authenticated Request

1. Create new request
2. Method: **POST**
3. URL: `http://localhost:8085/movies`
4. Go to **Authorization** tab
5. Type: **Bearer Token**
6. Token: **Paste your JWT token**
7. Go to **Body** tab → **raw** → **JSON**
8. Add:
```json
{
  "title": "The Matrix",
  "genre": "Sci-Fi",
  "releaseYear": 1999
}
```
9. Click **Send**

### 4. Logout

1. Create new request
2. Method: **POST**
3. URL: `http://localhost:8085/api/auth/logout`
4. Go to **Authorization** tab
5. Type: **Bearer Token**
6. Token: **Paste your JWT token**
7. Click **Send**

---

## 🔍 Complete API Reference

### Public Endpoints (No Authentication)

| Endpoint | Method | Description | Body |
|----------|--------|-------------|------|
| `/api/auth/register` | POST | Register new user | `{username, email, password}` |
| `/api/auth/login` | POST | Login and get JWT | `{username, password}` |
| `/movies` | GET | View all movies | None |
| `/movies/{id}` | GET | View movie by ID | None |

### Authenticated Endpoints (Requires JWT Token)

| Endpoint | Method | Role Required | Description |
|----------|--------|---------------|-------------|
| `/api/auth/logout` | POST | Any | Logout (blacklist token) |
| `/movies` | POST | Any User | Create movie |
| `/movies/{id}` | PUT | Any User | Update movie |
| `/movies/{id}` | DELETE | ADMIN | Delete movie |
| `/api/users` | GET | ADMIN | View all users |
| `/api/users/{id}` | GET | ADMIN | View user by ID |
| `/api/users/{id}` | PUT | ADMIN | Update user |
| `/api/users/{id}` | DELETE | ADMIN | Delete user |

---

## 🎯 Testing Scenarios

### Scenario 1: Complete User Flow

```bash
# 1. Register
curl -X POST http://localhost:8085/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@example.com","password":"alice123"}'

# 2. Login (save token)
TOKEN=$(curl -s -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"alice123"}' | jq -r '.token')

echo "Token: $TOKEN"

# 3. Create movie (authenticated)
curl -X POST http://localhost:8085/movies \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Interstellar","genre":"Sci-Fi","releaseYear":2014}'

# 4. View movies (public)
curl http://localhost:8085/movies

# 5. Logout
curl -X POST http://localhost:8085/api/auth/logout \
  -H "Authorization: Bearer $TOKEN"

# 6. Try to create movie with old token (should fail)
curl -X POST http://localhost:8085/movies \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Should Fail","genre":"Action"}'
```

---

### Scenario 2: Token Expiration

By default, tokens expire after 1 hour.

```bash
# Login
TOKEN=$(curl -s -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"alice123"}' | jq -r '.token')

# Use immediately (works)
curl -X POST http://localhost:8085/movies \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Test Movie"}'

# Wait 1 hour... token expires
# Try again (fails with 401)
curl -X POST http://localhost:8085/movies \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Expired Token"}'
```

**To test faster, change expiration in application-local.properties:**
```properties
jwt.expiration=60000  # 1 minute for testing
```

---

### Scenario 3: Admin Operations

First, create an admin user in database:

```sql
-- Register a user first via API, then:
INSERT INTO user_roles (user_id, role) VALUES (1, 'ROLE_ADMIN');
```

Then test admin operations:

```bash
# Login as admin
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin_user","password":"admin123"}' | jq -r '.token')

# View all users (admin only)
curl http://localhost:8085/api/users \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# Delete a movie (admin only)
curl -X DELETE http://localhost:8085/movies/1 \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# Delete a user (admin only)
curl -X DELETE http://localhost:8085/api/users/2 \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

## 🔧 Configuration

### JWT Settings (application-local.properties)

```properties
# Secret key - MUST be long enough for HS256 (256-bit)
jwt.secret=mySecretKeyForJWTTokenSigningMustBeLongEnoughForHS256Algorithm

# Token expiration (milliseconds)
jwt.expiration=3600000  # 1 hour

# Change to 60000 for 1-minute tokens (testing)
# Change to 86400000 for 24-hour tokens (production)
```

---

## ❌ Common Errors & Solutions

### Error: 401 Unauthorized
**Causes:**
- Wrong username/password
- Token expired
- Token blacklisted (after logout)
- No token provided

**Solution:**
- Check credentials
- Login again to get new token
- Ensure token is in `Authorization: Bearer TOKEN` format

---

### Error: 403 Forbidden
**Causes:**
- User doesn't have required role
- Regular user trying admin operation

**Solution:**
- Check user roles in database
- Add required role to user

---

### Error: Invalid JWT token
**Causes:**
- Malformed token
- Token signature invalid
- Wrong secret key

**Solution:**
- Get new token from login endpoint
- Check jwt.secret in properties matches

---

### Error: Token blacklisted
**Causes:**
- User logged out
- Using token after logout

**Solution:**
- Login again to get new token

---

## 🔐 Security Best Practices

### 1. JWT Secret Key
```properties
# ❌ BAD - Too short
jwt.secret=secret123

# ✅ GOOD - Long and random
jwt.secret=7vN2K8pQ5xR9wM3jL6hF4dS1aY0tE8uI7oP5qW2mX3nV1cB6zK9jH4gF7dS0aQ3w
```

**Generate secure key:**
```bash
openssl rand -base64 64
```

### 2. Token Expiration
```properties
# ❌ BAD - Too long (7 days)
jwt.expiration=604800000

# ✅ GOOD - Short-lived (1 hour)
jwt.expiration=3600000
```

### 3. HTTPS Only
- Always use HTTPS in production
- Tokens can be intercepted over HTTP

### 4. Store Tokens Securely (Frontend)
```javascript
// ❌ BAD - localStorage (vulnerable to XSS)
localStorage.setItem('token', token);

// ✅ BETTER - sessionStorage (cleared on tab close)
sessionStorage.setItem('token', token);

// ✅ BEST - httpOnly cookie (can't access via JS)
// Requires backend changes to send token in cookie
```

---

## 📊 JWT vs Basic Auth Comparison

| Feature | Basic Auth | JWT |
|---------|-----------|-----|
| **Authentication** | Username:password every request | Token once, reuse |
| **Stateless** | ❌ No | ✅ Yes |
| **Logout** | ❌ No server-side logout | ✅ Token blacklist |
| **Expiration** | ❌ No | ✅ Configurable |
| **Scalability** | ⭐⭐ Okay | ⭐⭐⭐⭐⭐ Excellent |
| **Mobile-Friendly** | ⭐⭐ Okay | ⭐⭐⭐⭐⭐ Excellent |
| **Security** | ⭐⭐⭐ Good | ⭐⭐⭐⭐ Better |

---

## 🎓 Understanding JWT Token

### Token Structure

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTY5ODc2NTQzMiwiZXhwIjoxNjk4NzY5MDMyLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dfQ.xyz123...

↓ Decode at jwt.io ↓

Header: {
  "alg": "HS256",
  "typ": "JWT"
}

Payload: {
  "sub": "john_doe",         // Username
  "iat": 1698765432,          // Issued at
  "exp": 1698769032,          // Expires at
  "authorities": [            // Roles
    {"authority": "ROLE_USER"}
  ]
}

Signature: HMACSHA256(
  base64(header) + "." + base64(payload),
  secret-key
)
```

### What Happens on Each Request

```
1. Client sends: Authorization: Bearer <token>
   ↓
2. JwtAuthenticationFilter intercepts
   ↓
3. Extract token from header
   ↓
4. Validate token:
   - Check signature (not tampered)
   - Check expiration (not expired)
   - Check blacklist (not logged out)
   ↓
5. Extract username from token
   ↓
6. Load user from database
   ↓
7. Set authentication in SecurityContext
   ↓
8. Request proceeds to controller
```

---

## ✅ Testing Checklist

- [ ] Register new user
- [ ] Login and receive JWT token
- [ ] Use token to create movie (authenticated)
- [ ] Try without token (should fail with 401)
- [ ] Logout
- [ ] Try with old token (should fail - blacklisted)
- [ ] Login again (get new token)
- [ ] Test token expiration (change expiration to 1 minute)
- [ ] Test admin endpoints (need ROLE_ADMIN)
- [ ] Test regular user can't delete movies (403 Forbidden)

---

## 🎉 Success!

You now have a fully functional JWT authentication system with:

✅ User registration
✅ Login with JWT token generation
✅ Token-based authentication
✅ Proper logout with token blacklisting
✅ Token expiration
✅ Role-based authorization
✅ Stateless authentication (scalable)

**Next steps:** Build your frontend to use this API! 🚀

