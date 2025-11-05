package com.revature.movieapp.movieapp.controller;

import com.revature.movieapp.movieapp.dto.PersonDTO;
import com.revature.movieapp.movieapp.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Get all persons
     */
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        List<PersonDTO> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    /**
     * Search persons by name
     * Example: GET /api/persons/search?name=Christian
     */
    @GetMapping("/search")
    public ResponseEntity<List<PersonDTO>> searchPersons(@RequestParam String name) {
        List<PersonDTO> persons = personService.searchPersonsByName(name);
        return ResponseEntity.ok(persons);
    }

    /**
     * Get person by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        PersonDTO person = personService.getPersonById(id);
        return ResponseEntity.ok(person);
    }

    /**
     * Create a new person
     */
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        PersonDTO createdPerson = personService.createPerson(personDTO);
        return ResponseEntity.ok(createdPerson);
    }

    /**
     * Update an existing person
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(
            @PathVariable Long id,
            @Valid @RequestBody PersonDTO personDTO) {
        PersonDTO updatedPerson = personService.updatePerson(id, personDTO);
        return ResponseEntity.ok(updatedPerson);
    }

    /**
     * Delete a person
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}

