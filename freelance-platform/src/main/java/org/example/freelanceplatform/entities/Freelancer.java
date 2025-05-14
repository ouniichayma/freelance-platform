package org.example.freelanceplatform.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Freelancer {

    @Id @GeneratedValue
    private Long id;

    private String nom;
    private String email;
    private String bio;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<Skill> skills;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<SocialLink> links;
}
