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
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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

    @MutationMapping
    public Freelancer createFreelancer(@Argument String nom, @Argument String email, @Argument String bio) {
        Freelancer freelancer = new Freelancer();
        freelancer.setNom(nom);
        freelancer.setEmail(email);
        freelancer.setBio(bio);
        return freelancerRepository.save(freelancer);
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
}
