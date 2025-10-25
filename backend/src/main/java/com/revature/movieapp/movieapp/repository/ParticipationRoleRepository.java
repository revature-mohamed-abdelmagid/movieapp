package com.revature.movieapp.movieapp.repository;

import com.revature.movieapp.movieapp.model.ParticipationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRoleRepository extends JpaRepository<ParticipationRole, Long> {

    List<ParticipationRole> findByParticipationId(Long participationId);

    List<ParticipationRole> findByRoleId(Long roleId);

}
