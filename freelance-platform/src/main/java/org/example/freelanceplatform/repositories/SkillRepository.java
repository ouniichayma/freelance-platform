package org.example.freelanceplatform.repositories;

import org.example.freelanceplatform.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {}