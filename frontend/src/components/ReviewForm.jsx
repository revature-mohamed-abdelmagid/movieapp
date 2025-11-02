import React, { useState } from 'react';
import { FaStar } from 'react-icons/fa';

const ReviewForm = () => {
  const [rating, setRating] = useState(0);
  const [reviewText, setReviewText] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Submit to backend/API
    console.log({ rating, reviewText });
  };

  return (
    <form className="review-form" onSubmit={handleSubmit}>
      <h3>Write a Review</h3>
      <div className="rating mb-4 mt-4">
        Your Rating
        {[...Array(5)].map((_, index) => (
          <FaStar
            key={index}
            className="star"
            color={index < rating ? '#ffc107' : '#e4e5e9'}
            onClick={() => setRating(index + 1)}
          />
        ))}
      </div>
    <textarea
    className="w-full h-20 bg-[#333] text-white border-none p-2 rounded mb-4 placeholder-gray-500"
    placeholder="Share your thoughts about this movie..."
    value={reviewText}
    onChange={(e) => setReviewText(e.target.value)}
  />
  <button type="submit" className="bg-[#ffc107] text-black border-none py-2 px-4 cursor-pointer rounded">Submit Review</button>
    </form>
  );
};

export default ReviewForm;