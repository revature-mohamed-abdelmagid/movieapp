# 🚀 Quick Start Guide - Spring Security

## ⚡ 5-Minute Setup

### 1. Start Your Application

```bash
cd /Users/mohamedabdelmagid/Documents/moviespring/backend
./mvnw spring-boot:run
```

Wait for the application to start (look for "Started MovieappApplication").

### 2. Test Public Endpoint (No Auth Required)

Open Postman or use curl:

```bash
# Get all movies (public endpoint)
curl http://localhost:8085/movies
```

✅ Should work without authentication

### 3. Register Your First User

```bash
curl -X POST http://localhost:8085/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

✅ You should see a success response with user details

### 4. Try Protected Endpoint (Will Fail)

```bash
# Try to create a movie without authentication
curl -X POST http://localhost:8085/movies \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Inception",
    "genre": "Sci-Fi"
  }'
```

❌ Should return 401 Unauthorized

### 5. Login and Access Protected Endpoint

```bash
# Create movie with authentication (use your registered username:password)
curl -X POST http://localhost:8085/movies \
  -H "Content-Type: application/json" \
  -u testuser:password123 \
  -d '{
    "title": "Inception",
    "genre": "Sci-Fi",
    "releaseYear": 2010
  }'
```

✅ Should create the movie successfully!

### 6. Try Admin Endpoint (Will Fail)

```bash
# Try to delete movie (requires ADMIN role)
curl -X DELETE http://localhost:8085/movies/1 \
  -u testuser:password123
```

❌ Should return 403 Forbidden (you need ADMIN role)

### 7. Create Admin User (Manual - One Time)

Connect to your MySQL database:

```sql
-- Register admin user via API first, then update role:
-- OR insert directly:

INSERT INTO users (username, email, password, createdat) 
VALUES ('admin', 'admin@example.com', 
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
        UNIX_TIMESTAMP());

-- Password above is 'admin123' already encrypted
-- Add admin role:
INSERT INTO user_roles (user_id, role) 
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN');
```

### 8. Test Admin Functionality

```bash
# Delete movie as admin
curl -X DELETE http://localhost:8085/movies/1 \
  -u admin:admin123
```

✅ Should delete successfully!

```bash
# View all users (admin only)
curl http://localhost:8085/api/users \
  -u admin:admin123
```

✅ Should return all users!

---

## 📋 Postman Testing

### Setup in Postman:

1. **Create New Request**
2. **Set URL:** `http://localhost:8085/movies`
3. **Set Method:** POST
4. **Go to Authorization Tab:**
   - Type: Basic Auth
   - Username: `testuser`
   - Password: `password123`
5. **Go to Body Tab:**
   - Select: raw
   - Type: JSON
   - Add your JSON data

### Example Requests:

#### Register User
- Method: POST
- URL: `http://localhost:8085/api/auth/register`
- Auth: None
- Body:
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "mypassword"
}
```

#### Create Movie (Authenticated)
- Method: POST
- URL: `http://localhost:8085/movies`
- Auth: Basic Auth (testuser:password123)
- Body:
```json
{
  "title": "The Matrix",
  "genre": "Sci-Fi",
  "releaseYear": 1999
}
```

#### View All Users (Admin Only)
- Method: GET
- URL: `http://localhost:8085/api/users`
- Auth: Basic Auth (admin:admin123)
- Body: None

---

## 🎯 Access Control Summary

| Action | Endpoint | Required Role | Example User |
|--------|----------|---------------|--------------|
| Register | POST `/api/auth/register` | None (Public) | Anyone |
| View Movies | GET `/movies` | None (Public) | Anyone |
| Create Movie | POST `/movies` | Any User | testuser |
| Update Movie | PUT `/movies/{id}` | Any User | testuser |
| Delete Movie | DELETE `/movies/{id}` | ADMIN | admin |
| View Users | GET `/api/users` | ADMIN | admin |
| Delete User | DELETE `/api/users/{id}` | ADMIN | admin |

---

## 🔍 Troubleshooting

### "401 Unauthorized"
- Check username and password are correct
- Make sure you're sending Authorization header
- Verify user exists in database

### "403 Forbidden"
- User is logged in but lacks required role
- Check `user_roles` table for user's roles
- Admin endpoints require ROLE_ADMIN

### "User not found with username: X"
- User doesn't exist in database
- Register user first via `/api/auth/register`

### Password not working
- Make sure you're using the correct password
- Password is case-sensitive
- If you manually inserted user, ensure password is BCrypt encoded

---

## 📚 Next Steps

✅ **You've completed:** Basic authentication & authorization

🎯 **Try next:**
1. Add more roles (MODERATOR, PREMIUM_USER)
2. Add movie reviews with role-based access
3. Implement JWT tokens
4. Add password reset functionality
5. Add email verification

📖 **Read more:**
- `SECURITY_TESTING_GUIDE.md` - Detailed testing instructions
- `SECURITY_IMPLEMENTATION_SUMMARY.md` - How everything works

---

**That's it! You now have working Spring Security! 🎉**

