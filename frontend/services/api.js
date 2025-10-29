import axios from 'axios';

// Configure axios base URL - adjust port as needed
const API_BASE_URL = 'http://localhost:8080'; // Spring Boot runs on port 8080 (from application-local.properties)

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for logging
api.interceptors.request.use(
  (config) => {
    console.log(`Making ${config.method?.toUpperCase()} request to: ${config.url}`);
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
  getReviewsByMovie: (movieId) => api.get(`/movies/${movieId}/reviews`),
  createReview: (reviewData) => api.post('/reviews', reviewData),
  updateReview: (id, reviewData) => api.put(`/reviews/${id}`, reviewData),
  deleteReview: (id) => api.delete(`/reviews/${id}`),
};

export default api;