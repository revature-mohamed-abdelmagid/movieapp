import React from 'react';
import { FaStar } from 'react-icons/fa';

const ReviewsList = ({ movie }) => {
  const reviews = movie.reviews || [];
  console.log('ReviewsList reviews:', reviews);
  console.log(reviews);
  if (reviews.length === 0) {
    
    return <p>No reviews yet. Be the first to review!</p>;
  }
  return (
    <>
    <div className="space-y-4 ">
            {reviews.map((review) => (
              <div key={review.reviewId} className="p-6 border border-gray-700 rounded-lg bg-gray-900">
                <div className="flex items-start justify-between mb-2">
                  <div className='flex flex-col text-left'>
                    <p className="font-semibold">{review.userName}</p>
                    <p className="text-sm text-muted-foreground">
                      {new Date(review.updatedAt).toLocaleDateString()}
                    </p>
                  </div>
                  <div className="flex items-center gap-1">
                    <FaStar className="h-4 w-4 fill-primary text-yellow-500" />
                    <span className="font-semibold">{review.rating}</span>
                  </div>
                </div>
                <p className="text-muted-foreground text-left text-gray-500">{review.reviewText}</p>
              </div>
            ))}

            {reviews.length === 0 && (
              <div className="p-6 text-center">
                <p className="text-muted-foreground">No reviews yet. Be the first to review!</p>
              </div>
            )}
          </div>
      </>
    
  );
};

export default ReviewsList;