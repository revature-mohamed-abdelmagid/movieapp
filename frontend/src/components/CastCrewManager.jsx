// src/components/CastCrewManager.jsx
import React, { useState, useEffect } from 'react';
import { personAPI, roleAPI } from '../../services/api';
import '../styles/CastCrewManager.css';

const CastCrewManager = ({ castCrew, setCastCrew }) => {
  const [roles, setRoles] = useState([]);
  const [showAddForm, setShowAddForm] = useState(false);
  const [showCreatePerson, setShowCreatePerson] = useState(false);
  
  // Search state
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [searching, setSearching] = useState(false);
  
  // Form state for adding cast/crew
  const [selectedPerson, setSelectedPerson] = useState(null);
  const [selectedRole, setSelectedRole] = useState('');
  const [characterName, setCharacterName] = useState('');
  
  // Form state for creating new person
  const [newPerson, setNewPerson] = useState({
    name: '',
    birthDate: '',
    bio: '',
    profileUrl: ''
  });

  // Fetch roles on component mount
  useEffect(() => {
    fetchRoles();
  }, []);

  const fetchRoles = async () => {
    try {
      const response = await roleAPI.getAllRoles();
      setRoles(response.data);
    } catch (err) {
      console.error('Failed to fetch roles:', err);
    }
  };

  // Search for persons
  const handleSearch = async (query) => {
    setSearchQuery(query);
    
    if (query.length < 2) {
      setSearchResults([]);
      return;
    }

    setSearching(true);
    try {
      const response = await personAPI.searchPersons(query);
      setSearchResults(response.data);
    } catch (err) {
      console.error('Search failed:', err);
      setSearchResults([]);
    } finally {
      setSearching(false);
    }
  };

  // Select a person from search results
  const handleSelectPerson = (person) => {
    setSelectedPerson(person);
    setSearchQuery(person.name);
    setSearchResults([]);
  };

  // Add cast/crew member to the list
  const handleAddCastCrew = () => {
    if (!selectedPerson || !selectedRole) {
      alert('Please select a person and role');
      return;
    }

    const roleName = roles.find(r => r.roleId === parseInt(selectedRole))?.name || '';

    const newMember = {
      personId: selectedPerson.personId,
      personName: selectedPerson.name,
      roleId: parseInt(selectedRole),
      roleName: roleName,
      characterName: characterName || null
    };

    setCastCrew([...castCrew, newMember]);
    
    // Reset form
    setSelectedPerson(null);
    setSearchQuery('');
    setSelectedRole('');
    setCharacterName('');
    setShowAddForm(false);
  };

  // Create new person
  const handleCreatePerson = async () => {
    if (!newPerson.name.trim()) {
      alert('Person name is required');
      return;
    }

    try {
      // Debug: Check if token exists
      const token = localStorage.getItem('token');
      console.log('Token exists:', !!token);
      console.log('Token value:', token ? token.substring(0, 20) + '...' : 'No token');

      const personData = {
        name: newPerson.name.trim(),
        birthDate: newPerson.birthDate || null,
        bio: newPerson.bio.trim() || null,
        profileUrl: newPerson.profileUrl.trim() || null
      };

      console.log('Creating person with data:', personData);
      const response = await personAPI.createPerson(personData);
      
      // Select the newly created person
      handleSelectPerson(response.data);
      
      // Reset form
      setNewPerson({ name: '', birthDate: '', bio: '', profileUrl: '' });
      setShowCreatePerson(false);
    } catch (err) {
      console.error('Failed to create person:', err);
      console.error('Error response:', err.response);
      console.error('Error status:', err.response?.status);
      console.error('Error data:', err.response?.data);
      
      let errorMessage = 'Failed to create person. ';
      if (err.response?.status === 403) {
        errorMessage += 'Access denied. Make sure you are logged in as an admin.';
      } else if (err.response?.data?.message) {
        errorMessage += err.response.data.message;
      } else {
        errorMessage += err.message;
      }
      
      alert(errorMessage);
    }
  };

  // Remove cast/crew member
  const handleRemoveMember = (index) => {
    setCastCrew(castCrew.filter((_, i) => i !== index));
  };

  return (
    <div className="cast-crew-manager">
      <div className="cast-crew-header">
        <h3>Cast & Crew</h3>
        <button
          type="button"
          className="btn-add-member"
          onClick={() => setShowAddForm(!showAddForm)}
        >
          {showAddForm ? '✕ Cancel' : '+ Add Cast/Crew'}
        </button>
      </div>

      {/* Add Cast/Crew Form */}
      {showAddForm && (
        <div className="add-cast-form">
          <div className="form-row">
            <div className="form-group">
              <label>Search Person</label>
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => handleSearch(e.target.value)}
                placeholder="Type to search (e.g., Christian Bale)..."
                className="search-input"
              />
              
              {/* Search Results Dropdown */}
              {searchResults.length > 0 && (
                <div className="search-results">
                  {searchResults.map((person) => (
                    <div
                      key={person.personId}
                      className="search-result-item"
                      onClick={() => handleSelectPerson(person)}
                    >
                      <div className="person-info">
                        <strong>{person.name}</strong>
                        {person.birthDate && <span> • Born: {person.birthDate}</span>}
                      </div>
                    </div>
                  ))}
                </div>
              )}

              {searching && <small>Searching...</small>}
              
              {searchQuery.length >= 2 && searchResults.length === 0 && !searching && (
                <div className="no-results">
                  <small>No results found.</small>
                  <button
                    type="button"
                    className="btn-create-person"
                    onClick={() => setShowCreatePerson(true)}
                  >
                    Create "{searchQuery}"
                  </button>
                </div>
              )}
            </div>

            <div className="form-group">
              <label>Role</label>
              <select
                value={selectedRole}
                onChange={(e) => setSelectedRole(e.target.value)}
                className="role-select"
              >
                <option value="">Select Role</option>
                {roles.map((role) => (
                  <option key={role.roleId} value={role.roleId}>
                    {role.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group">
            <label>Character Name (for actors)</label>
            <input
              type="text"
              value={characterName}
              onChange={(e) => setCharacterName(e.target.value)}
              placeholder="e.g., Bruce Wayne"
              className="character-input"
            />
            <small>Leave empty for directors and crew</small>
          </div>

          <button
            type="button"
            className="btn-confirm-add"
            onClick={handleAddCastCrew}
            disabled={!selectedPerson || !selectedRole}
          >
            Add to List
          </button>
        </div>
      )}

      {/* Create Person Modal */}
      {showCreatePerson && (
        <div className="create-person-modal">
          <div className="modal-content">
            <div className="modal-header">
              <h4>Create New Person</h4>
              <button
                type="button"
                className="btn-close"
                onClick={() => setShowCreatePerson(false)}
              >
                ✕
              </button>
            </div>
            
            <div className="modal-body">
              <div className="form-group">
                <label>Name *</label>
                <input
                  type="text"
                  value={newPerson.name}
                  onChange={(e) => setNewPerson({ ...newPerson, name: e.target.value })}
                  placeholder="Full name"
                  maxLength={200}
                />
              </div>

              <div className="form-group">
                <label>Birth Date</label>
                <input
                  type="date"
                  value={newPerson.birthDate}
                  onChange={(e) => setNewPerson({ ...newPerson, birthDate: e.target.value })}
                />
              </div>

              <div className="form-group">
                <label>Profile Image URL</label>
                <input
                  type="url"
                  value={newPerson.profileUrl}
                  onChange={(e) => setNewPerson({ ...newPerson, profileUrl: e.target.value })}
                  placeholder="https://example.com/photo.jpg"
                />
              </div>

              <div className="form-group">
                <label>Biography</label>
                <textarea
                  value={newPerson.bio}
                  onChange={(e) => setNewPerson({ ...newPerson, bio: e.target.value })}
                  placeholder="Short biography..."
                  rows="3"
                  maxLength={5000}
                />
              </div>
            </div>

            <div className="modal-footer">
              <button
                type="button"
                className="btn-cancel"
                onClick={() => setShowCreatePerson(false)}
              >
                Cancel
              </button>
              <button
                type="button"
                className="btn-create"
                onClick={handleCreatePerson}
              >
                Create Person
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Cast/Crew List */}
      {castCrew.length > 0 && (
        <div className="cast-crew-list">
          <h4>Added Cast & Crew</h4>
          <div className="cast-crew-items">
            {castCrew.map((member, index) => (
              <div key={index} className="cast-crew-item">
                <div className="member-info">
                  <strong>{member.personName}</strong>
                  <span className="role-badge">{member.roleName}</span>
                  {member.characterName && (
                    <span className="character-name">as {member.characterName}</span>
                  )}
                </div>
                <button
                  type="button"
                  className="btn-remove"
                  onClick={() => handleRemoveMember(index)}
                >
                  ✕
                </button>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default CastCrewManager;

