import { useEffect } from 'react';
import { FaStar, FaClock } from 'react-icons/fa';
import { formatDuration } from '../utils/formatDuration.js';

export const TrailerPlayer = ({ movie, open, onOpenChange }) => {
  useEffect(() => {
    if (!open) return;

    const handleEsc = (e) => {
      if (e.key === 'Escape') {
        onOpenChange(false);
      }
    };

    document.addEventListener('keydown', handleEsc);
    return () => document.removeEventListener('keydown', handleEsc);
  }, [open, onOpenChange]);

  if (!open) return null;

  return (
    <div 
      className="fixed inset-0 bg-black/50 flex items-center justify-center z-50" 
      onClick={() => onOpenChange(false)}
    >
      <div 
        className="w-full sm:max-w-xl md:max-w-3xl lg:max-w-5xl h-[80vh] p-0 overflow-hidden bg-black/95 border border-primary/20 rounded-lg"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="p-6 bg-gradient-to-b from-background/80 to-transparent">
          <div className="space-y-3">
            <div className='flex flex-row items-stretch'>
            <h2 className="text-3xl font-bold text-left">{movie.title} - Trailer</h2>
            <button
              className="ml-auto mb-2 text-white text-2xl font-bold hover:text-gray-300 cursor-pointer"
              onClick={() => onOpenChange(false)}
            >
              &times;
            </button>
            </div>
            <div className="flex flex-wrap items-center gap-4 text-sm text-muted-foreground">
              <span>{movie.releaseYear}</span>
              <span>•</span>
              <div className="flex items-center gap-1">
                <FaStar className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                <span>{movie.avgRating.toFixed(1)}</span>
              </div>
              <span>•</span>
              <div className="flex items-center gap-1">
                <FaClock className="h-4 w-4" />
                <span>{formatDuration(movie.duration)}</span>
              </div>
              <span>•</span>
              <div className="flex flex-wrap gap-2">
                {movie.genres.map((g) => (
                  <span key={g.name} className="px-2 py-0.5 rounded-full bg-gray-800 text-white text-xs">
                    {g.name}
                  </span>
                ))}
              </div>
            </div>
          </div>
        </div>
        
        <div className="w-full h-full flex items-start justify-center px-6">
          <div className="w-full aspect-video rounded-lg overflow-hidden shadow-2xl">
            <iframe
              src={movie.trailerUrl}
              title={`${movie.title} Trailer`}
              className="w-full h-full"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          </div>
        </div>
      </div>
    </div>
  );
};