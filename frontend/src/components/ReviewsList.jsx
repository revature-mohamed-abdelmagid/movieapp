import React, { useState } from 'react';
import { FaStar, FaEdit, FaTrash, FaSave, FaTimes } from 'react-icons/fa';
import { reviewAPI } from '../../services/api';
import { useAuth } from '../context/AuthContext';

const ReviewsList = ({ movie, onReviewUpdated }) => {
  const { user } = useAuth();
  const reviews = movie.reviews || [];
  
  const [editingReviewId, setEditingReviewId] = useState(null);
  const [editRating, setEditRating] = useState(0);
  const [editText, setEditText] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleEditClick = (review) => {
    setEditingReviewId(review.reviewId);
    setEditRating(review.rating);
    setEditText(review.reviewText);
    setError(null);
  };

  const handleCancelEdit = () => {
    setEditingReviewId(null);
    setEditRating(0);
    setEditText('');
    setError(null);
  };

  const handleSaveEdit = async (reviewId) => {
    if (editRating === 0 || !editText.trim()) {
      setError('Please provide both a rating and review text');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await reviewAPI.updateReview(reviewId, {
        rating: editRating,
        reviewText: editText.trim()
      });

      setEditingReviewId(null);
      if (onReviewUpdated) {
        onReviewUpdated();
      }
    } catch (err) {
      console.error('Failed to update review:', err);
      setError('Failed to update review. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (reviewId) => {
    if (!window.confirm('Are you sure you want to delete this review?')) {
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await reviewAPI.deleteReview(reviewId);
      
      if (onReviewUpdated) {
        onReviewUpdated();
      }
    } catch (err) {
      console.error('Failed to delete review:', err);
      setError('Failed to delete review. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (reviews.length === 0) {
    return (
      <div className="no-reviews">
        <p>No reviews yet. Be the first to review!</p>
      </div>
    );
  }

  return (
    <div className="reviews-list">
      {error && (
        <div className="review-error">
          {error}
        </div>
      )}
      
      {reviews.map((review) => {
        const isEditing = editingReviewId === review.reviewId;
        const isOwnReview = user && user.id === review.userId;

        return (
          <div key={review.reviewId} className="review-card">
            <div className="review-header">
              <div className="review-author-info">
                <p className="review-author">{review.userName}</p>
                <p className="review-date">
                  {new Date(review.updatedAt || review.createdAt).toLocaleDateString('en-US', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                  })}
                </p>
              </div>
              
              <div className="review-rating-actions">
                <div className="review-rating">
                  {isEditing ? (
                    <div className="rating-edit">
                      {[...Array(5)].map((_, index) => (
                        <FaStar
                          key={index}
                          className="star-edit"
                          color={index < editRating ? '#ffd700' : '#333'}
                          onClick={() => setEditRating(index + 1)}
                        />
                      ))}
                    </div>
                  ) : (
                    <>
                      <FaStar className="star-icon" />
                      <span className="rating-value">{review.rating}</span>
                    </>
                  )}
                </div>
                
                {isOwnReview && !isEditing && (
                  <div className="review-actions">
                    <button 
                      className="btn-edit"
                      onClick={() => handleEditClick(review)}
                      disabled={loading}
                      title="Edit review"
                    >
                      <FaEdit />
                    </button>
                    <button 
                      className="btn-delete"
                      onClick={() => handleDelete(review.reviewId)}
                      disabled={loading}
                      title="Delete review"
                    >
                      <FaTrash />
                    </button>
                  </div>
                )}
              </div>
            </div>

            {isEditing ? (
              <div className="review-edit-form">
                <textarea
                  className="edit-textarea"
                  value={editText}
                  onChange={(e) => setEditText(e.target.value)}
                  disabled={loading}
                  maxLength={5000}
                />
                <div className="edit-actions">
                  <button 
                    className="btn-save"
                    onClick={() => handleSaveEdit(review.reviewId)}
                    disabled={loading}
                  >
                    <FaSave /> {loading ? 'Saving...' : 'Save'}
                  </button>
                  <button 
                    className="btn-cancel"
                    onClick={handleCancelEdit}
                    disabled={loading}
                  >
                    <FaTimes /> Cancel
                  </button>
                </div>
              </div>
            ) : (
              <p className="review-text">{review.reviewText}</p>
            )}
          </div>
        );
      })}
    </div>
  );
};

export default ReviewsList;