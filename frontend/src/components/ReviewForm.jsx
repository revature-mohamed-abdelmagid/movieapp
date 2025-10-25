// src/components/ReviewForm.jsx
import React, { useState } from 'react';
import { reviewAPI } from '../services/api';

const ReviewForm = ({ movieId, onReviewAdded }) => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    
    try {
      await reviewAPI.createReview({
        movieId,
        rating,
        comment,
        userId: 1 // In real app, this would come from auth
      });
      
      setRating(0);
      setComment('');
      onReviewAdded?.();
    } catch (error) {
      console.error('Error submitting review:', error);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="review-form">
      <h3>Add Your Review</h3>
      <div className="rating-input">
        <label>Rating:</label>
        <select 
          value={rating} 
          onChange={(e) => setRating(Number(e.target.value))}
          required
        >
          <option value={0}>Select rating</option>
          {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(num => (
            <option key={num} value={num}>{num}</option>
          ))}
        </select>
      </div>
      <div className="comment-input">
        <label>Comment:</label>
        <textarea 
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          rows="4"
          required
        />
      </div>
      <button type="submit" disabled={submitting}>
        {submitting ? 'Submitting...' : 'Submit Review'}
      </button>
    </form>
  );
};

export default ReviewForm;