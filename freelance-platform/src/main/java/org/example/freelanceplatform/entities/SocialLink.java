package org.example.freelanceplatform.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class SocialLink {

    @Id
    @GeneratedValue
    private Long id;

    private String url;
    private String type; // ex: LinkedIn, GitHub

    @ManyToOne
    private Freelancer freelancer;
}
