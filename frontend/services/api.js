import axios from 'axios';

// Configure axios base URL - adjust port as needed
const API_BASE_URL = 'http://localhost:8080'; // Spring Boot runs on port 8080 (from application-local.properties)

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for logging and adding JWT token
api.interceptors.request.use(
  (config) => {
    console.log(`Making ${config.method?.toUpperCase()} request to: ${config.url}`);
    
    // Add JWT token to request headers if it exists
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('✓ JWT token attached to request');
    } else {
      console.warn('⚠ No JWT token found in localStorage');
    }
    
    // Debug: Log user info from localStorage
    const userStr = localStorage.getItem('user');
    if (userStr) {
      const user = JSON.parse(userStr);
      console.log('User roles:', user.roles);
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// Movie API calls
export const movieAPI = {
  getAllMovies: () => api.get('/movies'),
  getAllMoviesWithGenres: () => api.get('/movies/with-genres'),
  getMovieById: (id) => api.get(`/movies/${id}`),
  
  // Get movie with full details (genres, cast, crew with their information)
  getMovieFullDetails: (id) => api.get(`/movies/full-details/${id}`),
  
  // Get all movies with full details
  getAllMoviesWithFullDetails: () => api.get('/movies/full-details'),
  
  createMovie: (movieData) => api.post('/movies', movieData),
  updateMovie: (id, movieData) => api.put(`/movies/${id}`, movieData),
  deleteMovie: (id) => api.delete(`/movies/${id}`),
};

// Auth API calls
export const authAPI = {
  register: (userData) => api.post('/api/auth/register', userData),
  login: (credentials) => api.post('/api/auth/login', credentials),
  logout: (token) => api.post('/api/auth/logout', {}, {
    headers: { Authorization: `Bearer ${token}` }
  }),
};

// Review API calls
export const reviewAPI = {
  getAllReviews: () => api.get('/api/reviews'),
  getReviewById: (id) => api.get(`/api/reviews/${id}`),
  getReviewsByMovie: (movieId) => api.get(`/api/reviews/movie/${movieId}`),
  getReviewsByUser: (userId) => api.get(`/api/reviews/user/${userId}`),
  createReview: (reviewData) => api.post('/api/reviews', reviewData),
  updateReview: (id, reviewData) => api.put(`/api/reviews/${id}`, reviewData),
  patchReview: (id, reviewData) => api.patch(`/api/reviews/${id}`, reviewData),
  deleteReview: (id) => api.delete(`/api/reviews/${id}`),
};

// Person API calls (for cast/crew management)
export const personAPI = {
  getAllPersons: () => api.get('/api/persons'),
  searchPersons: (name) => api.get('/api/persons/search', { params: { name } }),
  getPersonById: (id) => api.get(`/api/persons/${id}`),
  createPerson: (personData) => api.post('/api/persons', personData),
  updatePerson: (id, personData) => api.put(`/api/persons/${id}`, personData),
  deletePerson: (id) => api.delete(`/api/persons/${id}`),
};

// Movie Cast/Crew API calls
export const movieCastAPI = {
  addCastToMovie: (movieId, castData) => api.post(`/api/movies/${movieId}/cast`, castData),
  addMultipleCastToMovie: (movieId, castList) => api.post(`/api/movies/${movieId}/cast/bulk`, castList),
  getCastForMovie: (movieId) => api.get(`/api/movies/${movieId}/cast`),
  removeCastFromMovie: (participationId) => api.delete(`/api/movies/cast/${participationId}`),
};

// Movie Role API calls
export const roleAPI = {
  getAllRoles: () => api.get('/api/roles'),
};

export default api;