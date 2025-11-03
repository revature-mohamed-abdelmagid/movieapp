import React, { useState } from 'react';
import { FaStar } from 'react-icons/fa';
import { reviewAPI } from '../../services/api';
import { useAuth } from '../context/AuthContext';

const ReviewForm = ({ movieId, onReviewSubmitted }) => {
  const { user, isAuthenticated } = useAuth();
  const [rating, setRating] = useState(0);
  const [reviewText, setReviewText] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validation
    if (!isAuthenticated) {
      setError('You must be logged in to submit a review');
      return;
    }

    if (rating === 0) {
      setError('Please select a rating');
      return;
    }

    if (!reviewText.trim()) {
      setError('Please write a review');
      return;
    }

    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      // Get user ID from localStorage
      const userStr = localStorage.getItem('user');
      const userData = userStr ? JSON.parse(userStr) : null;
      
      // We need to get the actual user ID from the database
      // For now, we'll use a workaround - check if user object has id
      const userId = userData?.id || user?.id;

      if (!userId) {
        setError('User ID not found. Please log out and log back in.');
        setLoading(false);
        return;
      }

      const reviewData = {
        movieId: movieId,
        userId: userId,
        rating: rating,
        reviewText: reviewText.trim()
      };

      console.log('Submitting review:', reviewData);
      await reviewAPI.createReview(reviewData);
      
      // Success!
      setSuccess(true);
      setRating(0);
      setReviewText('');
      
      // Call parent callback to refresh reviews
      if (onReviewSubmitted) {
        onReviewSubmitted();
      }

      // Clear success message after 3 seconds
      setTimeout(() => setSuccess(false), 3000);

    } catch (err) {
      console.error('Failed to submit review:', err);
      const errorMsg = err.response?.data?.message || 
                       err.response?.data || 
                       'Failed to submit review. Please try again.';
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="review-form p-4 bg-gray-800 rounded">
        <p className="text-gray-400">Please sign in to write a review</p>
      </div>
    );
  }

  return (
    <form className="review-form" onSubmit={handleSubmit}>
      <h3>Write a Review</h3>
      
      {error && (
        <div className="error-message bg-red-900 text-red-200 p-3 rounded mb-4">
          {error}
        </div>
      )}
      
      {success && (
        <div className="success-message bg-green-900 text-green-200 p-3 rounded mb-4">
          Review submitted successfully!
        </div>
      )}

      <div className="rating mb-4 mt-4">
        Your Rating
        {[...Array(5)].map((_, index) => (
          <FaStar
            key={index}
            className="star"
            color={index < rating ? '#ffc107' : '#e4e5e9'}
            onClick={() => setRating(index + 1)}
            style={{ cursor: 'pointer' }}
          />
        ))}
        {rating > 0 && <span className="ml-2 text-gray-400">({rating} star{rating > 1 ? 's' : ''})</span>}
      </div>
      
      <textarea
        className="w-full h-20 bg-[#333] text-white border-none p-2 rounded mb-4 placeholder-gray-500"
        placeholder="Share your thoughts about this movie..."
        value={reviewText}
        onChange={(e) => setReviewText(e.target.value)}
        disabled={loading}
        maxLength={5000}
      />
      
      <button 
        type="submit" 
        className="bg-[#ffc107] text-black border-none py-2 px-4 cursor-pointer rounded font-semibold"
        disabled={loading}
      >
        {loading ? 'Submitting...' : 'Submit Review'}
      </button>
    </form>
  );
};

export default ReviewForm;