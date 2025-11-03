import React, { useState, useEffect } from 'react';
import { movieAPI } from '../../services/api';
import '../styles/AdminDashboard.css';

const AdminDashboard = () => {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [formData, setFormData] = useState({
    title: '',
    releaseYear: new Date().getFullYear(),
    duration: 0,
    description: '',
    language: '',
    country: ''
  });

  useEffect(() => {
    fetchMovies();
  }, []);

  useEffect(() => {
    if (successMessage) {
      const timer = setTimeout(() => {
        setSuccessMessage('');
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [successMessage]);

  const fetchMovies = async () => {
    try {
      setLoading(true);
      const response = await movieAPI.getAllMovies();
      setMovies(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch movies');
      console.error('Error fetching movies:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'releaseYear' || name === 'duration' ? Number(value) : value
    }));
  };

  const handleAddMovie = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError(null);
    try {
      await movieAPI.createMovie(formData);
      setShowAddModal(false);
      await fetchMovies();
      setSuccessMessage('Movie added successfully');
      // Reset form
      setFormData({
        title: '',
        releaseYear: new Date().getFullYear(),
        duration: 0,
        description: '',
        language: '',
        country: ''
      });
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to add movie');
      console.error('Error adding movie:', err);
    } finally {
      setSaving(false);
    }
  };

  const handleEditMovie = async (e) => {
    e.preventDefault();
    if (!selectedMovie) return;

    setSaving(true);
    setError(null);
    try {
      await movieAPI.updateMovie(selectedMovie.movieId, formData);
      setShowEditModal(false);
      setSelectedMovie(null);
      await fetchMovies();
      setSuccessMessage('Movie updated successfully');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update movie');
      console.error('Error updating movie:', err);
    } finally {
      setSaving(false);
    }
  };

  const openEditModal = (movie) => {
    setSelectedMovie(movie);
    setFormData({
      title: movie.title,
      releaseYear: movie.releaseYear,
      duration: movie.duration,
      description: movie.description,
      language: movie.language,
      country: movie.country
    });
    setShowEditModal(true);
  };

  const handleDeleteMovie = async (movieId) => {
    if (!window.confirm('Are you sure you want to delete this movie?')) return;

    try {
      await movieAPI.deleteMovie(movieId);
      fetchMovies();
    } catch (err) {
      setError('Failed to delete movie');
      console.error('Error deleting movie:', err);
    }
  };

  if (loading) return <div className="admin-loading">Loading movies...</div>;

  return (
    <div className="admin-dashboard">
      <div className="admin-header">
        <h1>Movie Management</h1>
        <button className="add-movie-btn" onClick={() => setShowAddModal(true)}>
          Add New Movie
        </button>
      </div>

      {error && <div className="admin-error">{error}</div>}
      {successMessage && <div className="admin-success">{successMessage}</div>}

      <div className="movie-table-container">
        <table className="movie-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Year</th>
              <th>Duration</th>
              <th>Language</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {movies.map(movie => (
              <tr key={movie.movieId}>
                <td>{movie.title}</td>
                <td>{movie.releaseYear}</td>
                <td>{movie.duration} min</td>
                <td>{movie.language}</td>
                <td>
                  <button 
                    className="edit-btn"
                    onClick={() => openEditModal(movie)}
                  >
                    Edit
                  </button>
                  <button 
                    className="delete-btn"
                    onClick={() => handleDeleteMovie(movie.movieId)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Add Movie Modal */}
      {showAddModal && (
        <div className="modal">
          <div className="modal-content">
            <h2>Add New Movie</h2>
            <form onSubmit={handleAddMovie}>
              <div className="form-group">
                <label>Title:</label>
                <input
                  type="text"
                  name="title"
                  value={formData.title}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Release Year:</label>
                <input
                  type="number"
                  name="releaseYear"
                  value={formData.releaseYear}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Duration (minutes):</label>
                <input
                  type="number"
                  name="duration"
                  value={formData.duration}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Description:</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Language:</label>
                <input
                  type="text"
                  name="language"
                  value={formData.language}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Country:</label>
                <input
                  type="text"
                  name="country"
                  value={formData.country}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="modal-actions">
                <button type="submit">Add Movie</button>
                <button type="button" onClick={() => setShowAddModal(false)}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Edit Movie Modal */}
      {showEditModal && selectedMovie && (
        <div className="modal">
          <div className="modal-content">
            <h2>Edit Movie: {selectedMovie.title}</h2>
            <form onSubmit={handleEditMovie}>
              <div className="form-group">
                <label>Title:</label>
                <input
                  type="text"
                  name="title"
                  value={formData.title}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Release Year:</label>
                <input
                  type="number"
                  name="releaseYear"
                  value={formData.releaseYear}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Duration (minutes):</label>
                <input
                  type="number"
                  name="duration"
                  value={formData.duration}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Description:</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Language:</label>
                <input
                  type="text"
                  name="language"
                  value={formData.language}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Country:</label>
                <input
                  type="text"
                  name="country"
                  value={formData.country}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="modal-actions">
                <button type="submit">Save Changes</button>
                <button type="button" onClick={() => {
                  setShowEditModal(false);
                  setSelectedMovie(null);
                }}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;