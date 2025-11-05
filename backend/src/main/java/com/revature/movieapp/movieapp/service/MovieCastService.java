package com.revature.movieapp.movieapp.service;

import com.revature.movieapp.movieapp.dto.MovieCastCrewDTO;
import com.revature.movieapp.movieapp.model.MovieParticipation;
import com.revature.movieapp.movieapp.model.ParticipationRole;
import com.revature.movieapp.movieapp.repository.MovieParticipationRepository;
import com.revature.movieapp.movieapp.repository.MovieRepository;
import com.revature.movieapp.movieapp.repository.MovieRoleRepository;
import com.revature.movieapp.movieapp.repository.ParticipationRoleRepository;
import com.revature.movieapp.movieapp.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieCastService {

    private final MovieParticipationRepository participationRepository;
    private final ParticipationRoleRepository participationRoleRepository;
    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private final MovieRoleRepository roleRepository;

    public MovieCastService(
            MovieParticipationRepository participationRepository,
            ParticipationRoleRepository participationRoleRepository,
            MovieRepository movieRepository,
            PersonRepository personRepository,
            MovieRoleRepository roleRepository) {
        this.participationRepository = participationRepository;
        this.participationRoleRepository = participationRoleRepository;
        this.movieRepository = movieRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Add a person to a movie with a specific role
     * Example: Add Christian Bale as Actor (Bruce Wayne) to The Dark Knight
     */
    @Transactional
    public void addCastCrewToMovie(Long movieId, MovieCastCrewDTO castCrewDTO) {
        // Validate movie exists
        if (!movieRepository.existsById(movieId)) {
            throw new RuntimeException("Movie not found with id: " + movieId);
        }

        // Validate person exists
        if (!personRepository.existsById(castCrewDTO.getPersonId())) {
            throw new RuntimeException("Person not found with id: " + castCrewDTO.getPersonId());
        }

        // Validate role exists
        if (!roleRepository.existsById(castCrewDTO.getRoleId())) {
            throw new RuntimeException("Role not found with id: " + castCrewDTO.getRoleId());
        }

        // Create MovieParticipation (links person to movie)
        MovieParticipation participation = MovieParticipation.builder()
                .movieId(movieId)
                .personId(castCrewDTO.getPersonId())
                .build();
        
        MovieParticipation savedParticipation = participationRepository.save(participation);

        // Create ParticipationRole (links participation to role with character name)
        ParticipationRole participationRole = ParticipationRole.builder()
                .participationId(savedParticipation.getParticipationId())
                .roleId(castCrewDTO.getRoleId())
                .note(castCrewDTO.getCharacterName()) // Character name for actors, or other notes
                .build();

        participationRoleRepository.save(participationRole);
    }

    /**
     * Add multiple cast/crew members to a movie at once
     */
    @Transactional
    public void addMultipleCastCrewToMovie(Long movieId, List<MovieCastCrewDTO> castCrewList) {
        for (MovieCastCrewDTO castCrew : castCrewList) {
            addCastCrewToMovie(movieId, castCrew);
        }
    }

    /**
     * Remove a person's participation from a movie
     */
    @Transactional
    public void removeCastCrewFromMovie(Long participationId) {
        // First delete all participation roles
        List<ParticipationRole> participationRoles = 
                participationRoleRepository.findByParticipationId(participationId);
        participationRoleRepository.deleteAll(participationRoles);

        // Then delete the participation
        participationRepository.deleteById(participationId);
    }

    /**
     * Get all cast/crew for a movie
     */
    public List<MovieParticipation> getCastCrewForMovie(Long movieId) {
        return participationRepository.findByMovieId(movieId);
    }
}

