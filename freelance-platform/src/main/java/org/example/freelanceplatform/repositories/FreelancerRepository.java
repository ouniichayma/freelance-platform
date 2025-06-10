package org.example.freelanceplatform.repositories;

import org.example.freelanceplatform.entities.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    // Rechercher les freelancers qui ont un skill avec ce nom (exact match)
    @Query("SELECT DISTINCT f FROM Freelancer f JOIN f.skills s WHERE LOWER(s.nom) LIKE LOWER(CONCAT('%', :skillName, '%'))")
    List<Freelancer> findBySkillNameContainingIgnoreCase(@Param("skillName") String skillName);

}