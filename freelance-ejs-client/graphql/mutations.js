const { gql } = require('graphql-request');


const addSkill = async (client, { freelancerId, nom }) => {
  const mutation = gql`
    mutation ($freelancerId: ID!, $nom: String!) {
      addSkill(freelancerId: $freelancerId, nom: $nom) {
        id
        nom
      }
    }
  `;
  return await client.request(mutation, { freelancerId, nom });
};

const addSocialLink = async (client, freelancerId, url, type) => {
  const mutation = gql`
    mutation ($freelancerId: ID!, $url: String!, $type: String!) {
      addSocialLink(freelancerId: $freelancerId, url: $url, type: $type) {
        id
        url
        type
      }
    }
  `;
  return await client.request(mutation, { freelancerId, url, type });
};





const removeSkill = async (client, freelancerId, skillId) => {
  const mutation = gql`
    mutation ($freelancerId: ID!, $skillId: ID!) {
      removeSkillFromFreelancer(freelancerId: $freelancerId, skillId: $skillId)
    }
  `;
  return await client.request(mutation, { freelancerId, skillId });
};



const removeSocialLink = async (client, freelancerId, socialLinkId) => {
  const mutation = gql`
    mutation ($freelancerId: ID!, $socialLinkId: ID!) {
      removeSocialLinkFromFreelancer(freelancerId: $freelancerId, socialLinkId: $socialLinkId)
    }
  `;
  return await client.request(mutation, { freelancerId, socialLinkId });
};
















const updateFreelancer = async (
  client,
  { id, nom, email, bio, skills, urls, types }
) => {
  const mutation = gql`
    mutation (
      $id: ID!
      $nom: String!
      $email: String!
      $bio: String
      $skills: [String]
      $socialLinksUrls: [String]
      $socialLinksTypes: [String]
    ) {
      updateFreelancerWithDetails(
        id: $id
        nom: $nom
        email: $email
        bio: $bio
        skills: $skills
        socialLinksUrls: $socialLinksUrls
        socialLinksTypes: $socialLinksTypes
      ) {
        id
        nom
        email
      }
    }
  `;

  return await client.request(mutation, {
    id,
    nom,
    email,
    bio,
    skills,
    socialLinksUrls: urls,
    socialLinksTypes: types
  });
};






module.exports = { addSkill, addSocialLink,removeSocialLink,removeSkill,updateFreelancer };
