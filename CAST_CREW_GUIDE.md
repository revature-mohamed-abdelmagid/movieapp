# Cast & Crew Management Guide

## Overview
This guide explains how to use the cast and crew management feature when adding movies to the database.

## Database Structure

The system uses four tables to manage cast and crew:

1. **persons** - Stores information about people (actors, directors, etc.)
2. **movie_participations** - Links a person to a movie
3. **roles** - Defines role types (Actor, Director, Producer, etc.)
4. **participation_roles** - Links a participation to a role with optional notes (e.g., character name)

### Example Flow
To add Christian Bale as Bruce Wayne (Actor) in The Dark Knight:
1. Person: "Christian Bale" (in `persons` table)
2. MovieParticipation: Links Christian Bale to The Dark Knight movie
3. ParticipationRole: Links the participation to the "Actor" role with note "Bruce Wayne"

## Initial Setup

### 1. Initialize Roles
Before adding cast/crew, you need to populate the roles table. Run this SQL:

```sql
INSERT INTO roles (name, description) VALUES 
('Actor', 'Performs in the movie'),
('Director', 'Directs the movie'),
('Producer', 'Produces the movie'),
('Writer', 'Writes the screenplay'),
('Cinematographer', 'Director of Photography'),
('Editor', 'Edits the movie'),
('Composer', 'Creates the musical score'),
('Production Designer', 'Designs the visual appearance');
```

Or use the provided SQL file:
```bash
# In your database client:
source backend/src/main/resources/initialize-roles.sql
```

### 2. Make Sure You Have Admin Access
Refer to `ADMIN_GUIDE.md` to create an admin user.

## Using the Feature

### Adding a Movie with Cast & Crew

1. **Login as Admin**
2. **Click "Add Movie"** in the navbar
3. **Fill in movie details** (title, year, etc.)
4. **Scroll to "Cast & Crew" section**
5. **Click "+ Add Cast/Crew"**

### Adding Existing People

1. **Type the person's name** in the search box
2. **Select from search results**
3. **Choose their role** (Actor, Director, etc.)
4. **Enter character name** (for actors only, e.g., "Bruce Wayne")
5. **Click "Add to List"**

### Creating New People

If a person doesn't exist:

1. **Type their name** in the search box
2. **Click "Create [name]"** button
3. **Fill in person details**:
   - Name (required)
   - Birth Date (optional)
   - Profile Image URL (optional)
   - Biography (optional)
4. **Click "Create Person"**
5. The person will be automatically selected
6. Continue with role selection

### Completing the Form

1. Add multiple cast/crew members as needed
2. Review the "Added Cast & Crew" list
3. Remove any mistakes using the ✕ button
4. **Click "Add Movie"** to submit

The system will:
- Create the movie
- Add all cast/crew members
- Link them with their roles and character names

## API Endpoints

### Person Management
- `GET /api/persons` - Get all persons
- `GET /api/persons/search?name={query}` - Search persons
- `POST /api/persons` - Create new person (admin only)
- `PUT /api/persons/{id}` - Update person (admin only)
- `DELETE /api/persons/{id}` - Delete person (admin only)

### Cast/Crew Management
- `POST /api/movies/{movieId}/cast` - Add single cast/crew member
- `POST /api/movies/{movieId}/cast/bulk` - Add multiple cast/crew members
- `GET /api/movies/{movieId}/cast` - Get all cast/crew for a movie
- `DELETE /api/movies/cast/{participationId}` - Remove cast/crew member

### Roles
- `GET /api/roles` - Get all available roles

## Examples

### Example 1: Adding The Dark Knight with Cast

**Movie Details:**
- Title: The Dark Knight
- Year: 2008
- Director: Christopher Nolan

**Cast & Crew:**
1. Christian Bale - Actor (Bruce Wayne / Batman)
2. Heath Ledger - Actor (The Joker)
3. Christopher Nolan - Director
4. Hans Zimmer - Composer

**Steps:**
1. Fill in movie title, year, etc.
2. Search "Christian Bale" → Select → Role: Actor → Character: "Bruce Wayne" → Add
3. Search "Heath Ledger" → Select → Role: Actor → Character: "The Joker" → Add
4. Search "Christopher Nolan" → Select → Role: Director → Character: (empty) → Add
5. Search "Hans Zimmer" → Select → Role: Composer → Character: (empty) → Add
6. Submit

### Example 2: Creating a New Person

If an actor doesn't exist in the database:

1. Search "Tom Holland"
2. No results → Click "Create Tom Holland"
3. Fill details:
   ```
   Name: Tom Holland
   Birth Date: 1996-06-01
   Profile URL: https://example.com/tom-holland.jpg
   Bio: British actor known for playing Spider-Man
   ```
4. Click "Create Person"
5. Continue with role selection

## Character Names

- **For Actors:** Always enter the character name (e.g., "Tony Stark", "Bruce Wayne")
- **For Directors/Crew:** Leave character name empty
- Character names are stored in the `note` field of `participation_roles` table

## Security

- Only users with `ROLE_ADMIN` can:
  - Create persons
  - Update persons
  - Delete persons
  - Add cast/crew to movies
- Anyone can view persons and roles

## Troubleshooting

### "Person already exists" error
- The system prevents duplicate names
- Search for the existing person instead

### Roles dropdown is empty
- Run the initialize-roles.sql script
- Verify roles exist: `SELECT * FROM roles;`

### Cast/crew not saving
- Check JWT token is valid
- Verify user has ROLE_ADMIN
- Check browser console for errors
- Verify movie was created successfully first

## Database Schema Reference

```sql
-- Person table
CREATE TABLE persons (
    person_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    birth_date DATE,
    bio TEXT(5000),
    profile_url VARCHAR(255),
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

-- Movie Participation table
CREATE TABLE movie_participations (
    participation_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    movie_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (person_id) REFERENCES persons(person_id)
);

-- Roles table
CREATE TABLE roles (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(1000)
);

-- Participation Roles table
CREATE TABLE participation_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    participation_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    note VARCHAR(500), -- Character name for actors
    FOREIGN KEY (participation_id) REFERENCES movie_participations(participation_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);
```

## Future Enhancements

Potential improvements:
- Bulk import persons from CSV/JSON
- Person profile pages
- Movie credit display on movie details page
- Person filmography pages
- Advanced search filters (by role, year, etc.)
- Image upload functionality

