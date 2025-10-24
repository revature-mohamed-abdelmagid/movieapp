package com.revature.movieapp.movieapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participation_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "participation_id", nullable = false)
    private Long participationId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Size(max = 500)
    private String note; // optional note about the participation role (e.g., "lead", "cameo")

}
