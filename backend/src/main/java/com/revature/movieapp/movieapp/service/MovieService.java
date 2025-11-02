package com.revature.movieapp.movieapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revature.movieapp.movieapp.dto.MovieFullDetailsDTO;
import com.revature.movieapp.movieapp.dto.MovieWithGenresDTO;
import com.revature.movieapp.movieapp.model.Genre;
import com.revature.movieapp.movieapp.model.Movie;
import com.revature.movieapp.movieapp.model.MovieGenres;
import com.revature.movieapp.movieapp.model.MovieParticipation;
import com.revature.movieapp.movieapp.model.MovieRole;
import com.revature.movieapp.movieapp.model.ParticipationRole;
import com.revature.movieapp.movieapp.model.Person;
import com.revature.movieapp.movieapp.model.Review;
import com.revature.movieapp.movieapp.model.User;
import com.revature.movieapp.movieapp.repository.GenreRepository;
import com.revature.movieapp.movieapp.repository.MovieGenresRepository;
import com.revature.movieapp.movieapp.repository.MovieParticipationRepository;
import com.revature.movieapp.movieapp.repository.MovieRepository;
import com.revature.movieapp.movieapp.repository.MovieRoleRepository;
import com.revature.movieapp.movieapp.repository.ParticipationRoleRepository;
import com.revature.movieapp.movieapp.repository.PersonRepository;
import com.revature.movieapp.movieapp.repository.ReviewRepository;
import com.revature.movieapp.movieapp.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MovieService {
    
    private final MovieRepository movieRepository;
    private final MovieGenresRepository movieGenresRepository;
    private final GenreRepository genreRepository;
    private final MovieParticipationRepository movieParticipationRepository;
    private final PersonRepository personRepository;
    private final ParticipationRoleRepository participationRoleRepository;
    private final MovieRoleRepository movieRoleRepository;
    // Assuming a ReviewRepository exists
    private final ReviewRepository reviewRepository;
    // Assuming a UserRepository exists
    private final UserRepository userRepository;
    
    public MovieService(MovieRepository movieRepository, 
                       MovieGenresRepository movieGenresRepository,
                       GenreRepository genreRepository,
                       MovieParticipationRepository movieParticipationRepository,
                       PersonRepository personRepository,
                       ParticipationRoleRepository participationRoleRepository,
                       MovieRoleRepository movieRoleRepository,
                       ReviewRepository reviewRepository,
                       UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.movieGenresRepository = movieGenresRepository;
        this.genreRepository = genreRepository;
        this.movieParticipationRepository = movieParticipationRepository;
        this.personRepository = personRepository;
        this.reviewRepository = reviewRepository;
        this.participationRoleRepository = participationRoleRepository;
        this.movieRoleRepository = movieRoleRepository;
        this.userRepository = userRepository;
    }


    // get all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // get all movies with genres
    public List<MovieWithGenresDTO> getAllMoviesWithGenres() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(this::convertToMovieWithGenresDTO).collect(Collectors.toList());
    }

    // Convert Movie to MovieWithGenresDTO
    private MovieWithGenresDTO convertToMovieWithGenresDTO(Movie movie) {
        List<MovieGenres> movieGenres = movieGenresRepository.findByMovieId(movie.getMovieId());
        List<String> genreNames = movieGenres.stream()
                .map(mg -> mg.getGenre().getGenreName())
                .collect(Collectors.toList());

        return new MovieWithGenresDTO(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getReleaseYear(),
                movie.getDuration(),
                movie.getDescription(),
                movie.getLanguage(),
                movie.getCountry(),
                movie.getPosterUrl(),
                movie.getAvgRating(),
                genreNames
        );
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie getMovieById(Long id) {
        
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public Movie updateMovie(Long id, Movie movie) {
        
        Movie existingMovie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setReleaseYear(movie.getReleaseYear());
        existingMovie.setDuration(movie.getDuration());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setLanguage(movie.getLanguage());
        existingMovie.setCountry(movie.getCountry());
        existingMovie.setPosterUrl(movie.getPosterUrl());
        existingMovie.setAvgRating(movie.getAvgRating());
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    // Get movie with full details including genres, cast, and crew
    public MovieFullDetailsDTO getMovieFullDetails(Long movieId) {
        // Get the movie
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));
        
        // For review I wanna have username not userid as well of the user who posted it, review contains only the userid but we need to fetch username from 
        // user repository based on that id
        List<MovieFullDetailsDTO.ReviewDTO> reviewDTOs = new ArrayList<>();
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        // i wanna have thee revieews to be sorted according to their updated at field in descending order
        reviews.sort((r1, r2) -> r2.getUpdatedAt().compareTo(r1.getUpdatedAt()));
        for (Review review : reviews) {
            // Assuming a UserRepository exists to fetch user details
            String userName = "User" + review.getUserId(); // Placeholder for actual username fetching logic
            User user = userRepository.findById(review.getUserId())
                    .orElse(null);
            if (user != null) {
                userName = user.getUsername();
            }
            MovieFullDetailsDTO.ReviewDTO reviewDTO = MovieFullDetailsDTO.ReviewDTO.builder()
                    .reviewId(review.getReviewId())
                    .userId(review.getUserId())
                    .movieId(review.getMovieId())
                    .rating(review.getRating())
                    .reviewText(review.getReviewText())
                    .helpfulCount(review.getHelpfulCount())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .userName(userName)
                    .build();
            reviewDTOs.add(reviewDTO);
        }
        

        // Get genres
        List<MovieGenres> movieGenres = movieGenresRepository.findByMovieId(movieId);
        List<MovieFullDetailsDTO.GenreDTO> genres = movieGenres.stream()
                .map(mg -> MovieFullDetailsDTO.GenreDTO.builder()
                        .genreId(mg.getGenre().getGenreId())
                        .name(mg.getGenre().getGenreName())
                        .description(mg.getGenre().getDescription())
                        .build())
                .collect(Collectors.toList());

        // Get movie participations
        List<MovieParticipation> participations = movieParticipationRepository.findByMovieId(movieId);

        // Group participations by role type
        List<MovieFullDetailsDTO.PersonParticipationDTO> cast = new ArrayList<>();
        List<MovieFullDetailsDTO.PersonParticipationDTO> directors = new ArrayList<>();
        List<MovieFullDetailsDTO.PersonParticipationDTO> producers = new ArrayList<>();
        List<MovieFullDetailsDTO.PersonParticipationDTO> writers = new ArrayList<>();

        for (MovieParticipation participation : participations) {
            Person person = personRepository.findById(participation.getPersonId())
                    .orElse(null);
            
            if (person == null) continue;

            // Get roles for this participation
            List<ParticipationRole> participationRoles = participationRoleRepository
                    .findByParticipationId(participation.getParticipationId());

            List<MovieFullDetailsDTO.RoleDTO> roles = participationRoles.stream()
                    .map(pr -> {
                        MovieRole movieRole = movieRoleRepository.findById(pr.getRoleId())
                                .orElse(null);
                        if (movieRole == null) return null;
                        
                        return MovieFullDetailsDTO.RoleDTO.builder()
                                .roleId(movieRole.getRoleId())
                                .roleName(movieRole.getName())
                                .roleDescription(movieRole.getDescription())
                                .note(pr.getNote())
                                .build();
                    })
                    .filter(role -> role != null)
                    .collect(Collectors.toList());

            MovieFullDetailsDTO.PersonParticipationDTO personDto = 
                    MovieFullDetailsDTO.PersonParticipationDTO.builder()
                            .personId(person.getPersonId())
                            .name(person.getName())
                            .birthDate(person.getBirthDate())
                            .bio(person.getBio())
                            .profileUrl(person.getProfileUrl())
                            .roles(roles)
                            .build();

            // Categorize by role type
            for (MovieFullDetailsDTO.RoleDTO role : roles) {
                String roleName = role.getRoleName().toLowerCase();
                if (roleName.contains("actor") || roleName.contains("actress")) {
                    if (!cast.contains(personDto)) {
                        cast.add(personDto);
                    }
                } else if (roleName.contains("director")) {
                    if (!directors.contains(personDto)) {
                        directors.add(personDto);
                    }
                } else if (roleName.contains("producer")) {
                    if (!producers.contains(personDto)) {
                        producers.add(personDto);
                    }
                } else if (roleName.contains("writer") || roleName.contains("screenplay")) {
                    if (!writers.contains(personDto)) {
                        writers.add(personDto);
                    }
                }
            }
        }

        return MovieFullDetailsDTO.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .releaseYear(movie.getReleaseYear())
                .duration(movie.getDuration())
                .description(movie.getDescription())
                .language(movie.getLanguage())
                .country(movie.getCountry())
                .posterUrl(movie.getPosterUrl())
                .avgRating(movie.getAvgRating())
                .genres(genres)
                .cast(cast)
                .directors(directors)
                .producers(producers)
                .writers(writers)
                .reviews(reviewDTOs)
                .build();
    }

    // Get all movies with full details
    public List<MovieFullDetailsDTO> getAllMoviesWithFullDetails() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(movie -> getMovieFullDetails(movie.getMovieId()))
                .collect(Collectors.toList());
    }



}
