package org.example.freelanceplatform.graphql;

import org.example.freelanceplatform.entities.Freelancer;
import org.example.freelanceplatform.entities.Skill;
import org.example.freelanceplatform.entities.SocialLink;
import org.example.freelanceplatform.repositories.FreelancerRepository;
import org.example.freelanceplatform.repositories.SkillRepository;
import org.example.freelanceplatform.repositories.SocialLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
public class FreelancerGraphQL {

    @Autowired
    private FreelancerRepository freelancerRepository;



    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SocialLinkRepository socialLinkRepository;

    @QueryMapping
    public List<Freelancer> freelancers() {
        return freelancerRepository.findAll();
    }

    @QueryMapping
    public Freelancer freelancer(@Argument Long id) {
        return freelancerRepository.findById(id).orElse(null);
    }

    /*@MutationMapping
    public Freelancer createFreelancer(@Argument String nom, @Argument String email, @Argument String bio) {
        Freelancer freelancer = new Freelancer();
        freelancer.setNom(nom);
        freelancer.setEmail(email);
        freelancer.setBio(bio);
        return freelancerRepository.save(freelancer);
    }*/





    @MutationMapping
    public Freelancer createFreelancer(
            @Argument String nom,
            @Argument String email,
            @Argument String bio,
            @Argument String base64Image // image encodée en Base64
    ) throws IOException {
        Freelancer freelancer = new Freelancer();
        freelancer.setNom(nom);
        freelancer.setEmail(email);
        freelancer.setBio(bio);

        if (base64Image != null && !base64Image.isEmpty()) {
            // Générer un nom de fichier unique
            String filename = UUID.randomUUID().toString() + ".png";

            // Définir le répertoire de destination
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir); // Crée le dossier s'il n'existe pas
            }


            // Construire le chemin complet du fichier
            Path filePath = uploadDir.resolve(filename);

            // Décoder l'image Base64
            String cleanBase64 = base64Image.contains(",") ? base64Image.split(",")[1] : base64Image;
            byte[] imageBytes = Base64.getDecoder().decode(cleanBase64);

            // Écrire les octets dans le fichier
            Files.write(filePath, imageBytes);

            // Enregistrer le chemin du fichier dans l'entité

            String relativePath = "uploads/" + filename;
            freelancer.setImagePath(relativePath);
        }

        return freelancerRepository.save(freelancer);
    }



    @SchemaMapping(typeName = "Freelancer", field = "imageUrl")
    public String getImageUrl(Freelancer freelancer) {
        String path = freelancer.getImagePath().replace("\\", "/"); // 👈 remplace les anti-slash
        return "http://localhost:8080/" + path;
    }



    @MutationMapping
    public Boolean deleteFreelancer(@Argument Long id) {
        freelancerRepository.deleteById(id);
        return true;
    }






    @MutationMapping
    public Skill addSkill(@Argument Long freelancerId, @Argument String nom) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId).orElse(null);
        if (freelancer == null) return null;

        Skill skill = new Skill();
        skill.setNom(nom);
        skill.setFreelancer(freelancer);
        return skillRepository.save(skill);
    }

    @MutationMapping
    public SocialLink addSocialLink(@Argument Long freelancerId, @Argument String url, @Argument String type) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId).orElse(null);
        if (freelancer == null) return null;

        SocialLink link = new SocialLink();
        link.setUrl(url);
        link.setType(type);
        link.setFreelancer(freelancer);
        return socialLinkRepository.save(link);
    }



    @MutationMapping
    public Freelancer updateFreelancer(@Argument Long id, @Argument String nom, @Argument String email, @Argument String bio) {
        Freelancer freelancer = freelancerRepository.findById(id).orElse(null);
        if (freelancer == null) return null;
        freelancer.setNom(nom);
        freelancer.setEmail(email);
        freelancer.setBio(bio);
        return freelancerRepository.save(freelancer);
    }

    @QueryMapping
    public List<Skill> skills() {
        return skillRepository.findAll();
    }

    @QueryMapping
    public List<SocialLink> socialLinks() {
        return socialLinkRepository.findAll();
    }





    @QueryMapping
    public List<Freelancer> getFreelancersBySkill(@Argument String skillName) {
        System.out.println("Recherche avec skill: " + skillName);
        List<Freelancer> freelancers = freelancerRepository.findBySkillNameContainingIgnoreCase(skillName);
        System.out.println("Trouvé: " + freelancers.size() + " freelancers");
        return freelancers;
    }



    @MutationMapping
    public void removeSkillFromFreelancer(@Argument Long freelancerId, @Argument Long skillId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer introuvable"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill introuvable"));

        freelancer.getSkills().remove(skill);
        freelancerRepository.save(freelancer);
    }



    @MutationMapping
    public boolean removeSocialLinkFromFreelancer(@Argument Long freelancerId, @Argument Long socialLinkId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer introuvable"));

        SocialLink link = socialLinkRepository.findById(socialLinkId)
                .orElseThrow(() -> new RuntimeException("Lien social introuvable"));

        boolean removed = freelancer.getLinks().remove(link);
        freelancerRepository.save(freelancer);
        return removed;
    }








    @MutationMapping
    public Freelancer updateFreelancerWithDetails(
            @Argument Long id,
            @Argument String nom,
            @Argument String email,
            @Argument String bio,
            @Argument List<String> skills,
            @Argument List<String> socialLinksUrls,
            @Argument List<String> socialLinksTypes
    ) {
        Freelancer freelancer = freelancerRepository.findById(id).orElse(null);
        if (freelancer == null) return null;

        freelancer.setNom(nom);
        freelancer.setEmail(email);
        freelancer.setBio(bio);

        // Clear and update skills
        freelancer.getSkills().clear();
        for (String skillName : skills) {
            Skill skill = new Skill();
            skill.setNom(skillName);
            skill.setFreelancer(freelancer);
            freelancer.getSkills().add(skill);
        }

        // Clear and update social links
        freelancer.getLinks().clear();
        for (int i = 0; i < socialLinksUrls.size(); i++) {
            SocialLink link = new SocialLink();
            link.setUrl(socialLinksUrls.get(i));
            link.setType(socialLinksTypes.get(i));
            link.setFreelancer(freelancer);
            freelancer.getLinks().add(link);
        }

        return freelancerRepository.save(freelancer);
    }





}



