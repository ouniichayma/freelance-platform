const { gql } = require('graphql-request');

const getFreelancers = async (client) => {
  const query = gql`
    query {
      freelancers {
        id
        nom
        email
        bio
        imageUrl 
      }
    }
  `;
  const data = await client.request(query);
  return data.freelancers;
};

const getFreelancerById = async (client, id) => {
  const query = gql`
    query ($id: ID!) {
      freelancer(id: $id) {
        id
        nom
        email
        bio
        skills {
          id
          nom
        }
        links {
          id
          url
          type
        }
      }
    }
  `;
  const data = await client.request(query, { id });
  return data.freelancer;
};



const createFreelancer = async (client, { nom, email, bio, base64Image }) => {
  const mutation = gql`
    mutation ($nom: String!, $email: String!, $bio: String!, $base64Image: String) {
      createFreelancer(nom: $nom, email: $email, bio: $bio, base64Image: $base64Image) {
        id
      }
    }
  `;
  return await client.request(mutation, { nom, email, bio, base64Image });
};











const getFreelancersBySkill = async (client, skillName) => {
  const query = gql`
    query ($skillName: String!) {
      getFreelancersBySkill(skillName: $skillName) {
        id
        nom
        email
        bio
        skills {
          id
          nom
        }
        links {
          id
          url
          type
        }
      }
    }
  `;
  const data = await client.request(query, { skillName });
  return data.getFreelancersBySkill;  // <-- correction ici
};





module.exports = {
  getFreelancers,
  getFreelancerById,
  createFreelancer,
  getFreelancersBySkill
};
