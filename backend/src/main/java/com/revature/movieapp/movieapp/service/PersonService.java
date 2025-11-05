package com.revature.movieapp.movieapp.service;

import com.revature.movieapp.movieapp.dto.PersonDTO;
import com.revature.movieapp.movieapp.model.Person;
import com.revature.movieapp.movieapp.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Get all persons
     */
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search persons by name (case-insensitive partial match)
     */
    public List<PersonDTO> searchPersonsByName(String name) {
        return personRepository.findAll().stream()
                .filter(person -> person.getName().toLowerCase().contains(name.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get person by ID
     */
    public PersonDTO getPersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        return convertToDTO(person);
    }

    /**
     * Create a new person
     */
    @Transactional
    public PersonDTO createPerson(PersonDTO personDTO) {
        // Check if person with this name already exists
        personRepository.findByName(personDTO.getName())
                .ifPresent(existing -> {
                    throw new RuntimeException("Person with name '" + personDTO.getName() + "' already exists");
                });

        Person person = Person.builder()
                .name(personDTO.getName())
                .birthDate(personDTO.getBirthDate())
                .bio(personDTO.getBio())
                .profileUrl(personDTO.getProfileUrl())
                .build();

        Person savedPerson = personRepository.save(person);
        return convertToDTO(savedPerson);
    }

    /**
     * Update an existing person
     */
    @Transactional
    public PersonDTO updatePerson(Long id, PersonDTO personDTO) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));

        person.setName(personDTO.getName());
        person.setBirthDate(personDTO.getBirthDate());
        person.setBio(personDTO.getBio());
        person.setProfileUrl(personDTO.getProfileUrl());

        Person updatedPerson = personRepository.save(person);
        return convertToDTO(updatedPerson);
    }

    /**
     * Delete a person
     */
    @Transactional
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }

    /**
     * Convert Person entity to DTO
     */
    private PersonDTO convertToDTO(Person person) {
        return PersonDTO.builder()
                .personId(person.getPersonId())
                .name(person.getName())
                .birthDate(person.getBirthDate())
                .bio(person.getBio())
                .profileUrl(person.getProfileUrl())
                .build();
    }
}

