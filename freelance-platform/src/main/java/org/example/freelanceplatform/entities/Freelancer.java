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



    private String imagePath;





    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Skill> skills;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<SocialLink> links;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<SocialLink> getLinks() {
        return links;
    }

    public void setLinks(List<SocialLink> links) {
        this.links = links;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
