package com.revature.movieapp.movieapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for Person entity
 * Used for creating and returning person information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {
    
    private Long personId;
    
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters")
    private String name;
    
    private LocalDate birthDate;
    
    @Size(max = 5000, message = "Bio cannot exceed 5000 characters")
    private String bio;
    
    private String profileUrl;
}

