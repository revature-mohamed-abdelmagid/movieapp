-- Initialize Movie Roles
-- Run this script to populate the roles table with common movie roles

-- Clear existing roles (optional - comment out if you want to keep existing data)
-- DELETE FROM participation_roles;
-- DELETE FROM roles;

-- Insert common movie roles
INSERT INTO roles (role_id, name, description) VALUES 
(1, 'Actor', 'Performs in the movie'),
(2, 'Director', 'Directs the movie'),
(3, 'Producer', 'Produces the movie'),
(4, 'Writer', 'Writes the screenplay'),
(5, 'Cinematographer', 'Director of Photography'),
(6, 'Editor', 'Edits the movie'),
(7, 'Composer', 'Creates the musical score'),
(8, 'Production Designer', 'Designs the visual appearance');

-- Reset the auto-increment (optional - adjust based on your database)
-- For MySQL:
-- ALTER TABLE roles AUTO_INCREMENT = 9;

-- For PostgreSQL:
-- SELECT setval('roles_role_id_seq', 9);

