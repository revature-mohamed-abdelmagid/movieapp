# Admin Movie Management Guide

## Overview
This guide explains how to use the admin movie management feature in the CineVerse application.

## Features
- **Admin-Only Add Movie Page**: A beautiful form interface for adding movies to the database
- **Secure Access**: Only users with ROLE_ADMIN can access this feature
- **Form Validation**: All fields are validated according to the database constraints

## Making a User an Admin

To grant admin privileges to a user, you need to update the database directly:

### SQL Command:
```sql
-- First, find the user's ID
SELECT id, username FROM users WHERE username = 'your_username';

-- Then, insert the ROLE_ADMIN into the user_roles table
INSERT INTO user_roles (user_id, role) VALUES (<user_id>, 'ROLE_ADMIN');
```

### Example:
```sql
-- If the user ID is 1
INSERT INTO user_roles (user_id, role) VALUES (1, 'ROLE_ADMIN');
```

## Accessing the Add Movie Page

1. **Login** with an admin account
2. Look for the **"Add Movie"** button in the navigation bar (it only appears for admin users)
3. Click the button to access the Add Movie form

## Add Movie Form Fields

### Required Fields
- **Title** (1-50 characters)
- **Release Year** (Must be 1888 or later)

### Optional Fields
- **Duration** (1-600 minutes)
- **Language** (Max 50 characters)
- **Country** (Max 100 characters)
- **Poster URL** (Full URL to movie poster image)
- **Description** (Max 1000 characters)

## Form Validation

The form includes comprehensive validation:
- Title length between 1-50 characters
- Release year must be 1888 or later
- Duration must be between 1-600 minutes
- All text fields have maximum length limits
- URL fields validate proper URL format

## Success/Error Handling

- **Success**: When a movie is added successfully, you'll see a success message and be redirected to the home page after 2 seconds
- **Error**: If there's an error, a detailed error message will be displayed at the top of the form

## Security

- The backend enforces admin-only access through Spring Security
- JWT tokens are automatically included in all API requests
- Non-admin users are automatically redirected if they try to access the page
- All movie create/update/delete operations require ROLE_ADMIN

## Backend Endpoints

The following endpoints now require ROLE_ADMIN:
- `POST /movies/**` - Create movies
- `PUT /movies/**` - Update movies
- `DELETE /movies/**` - Delete movies

## Testing the Feature

1. Create a test admin user:
```sql
-- Register a user normally through the UI first, then:
INSERT INTO user_roles (user_id, role) 
VALUES (
  (SELECT id FROM users WHERE username = 'testadmin'),
  'ROLE_ADMIN'
);
```

2. Login with the admin account
3. Verify the "Add Movie" button appears in the navbar
4. Click and test the form
5. Check the database to confirm the movie was added

## Troubleshooting

### "Add Movie" button not appearing
- Verify the user has ROLE_ADMIN in the user_roles table
- Check browser console for any JavaScript errors
- Clear browser cache and reload

### Form submission fails
- Check browser console for error details
- Verify JWT token is valid (not expired)
- Check backend logs for security errors
- Ensure all required fields are filled

### 403 Forbidden Error
- User doesn't have ROLE_ADMIN
- JWT token is invalid or expired
- Try logging out and logging back in

## Future Enhancements

Potential improvements:
- Add genre selection with checkboxes
- Add cast and crew management
- File upload for poster images
- Bulk movie import from CSV/JSON
- Movie preview before saving
- Edit existing movies from the UI

