package org.example.freelanceplatform.repositories;

import org.example.freelanceplatform.entities.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {}