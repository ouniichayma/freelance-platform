const express = require('express');
const { GraphQLClient } = require('graphql-request');
const path = require('path');

const app = express();
const client = new GraphQLClient('http://localhost:8080/graphql');

app.use(express.urlencoded({ extended: true }));
app.use(express.static('public'));

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

const PORT = 3000;

const { getFreelancers, getFreelancerById, createFreelancer, } = require('./graphql/queries');
const { getFreelancersBySkill } = require('./graphql/queries');

const { addSkill, addSocialLink,removeSkill,removeSocialLink } = require('./graphql/mutations');
const { updateFreelancer } = require('./graphql/mutations'); 

// Page d’accueil

const methodOverride = require('method-override');
app.use(methodOverride('_method'));

app.get('/', async (req, res) => {
  const freelancers = await getFreelancers(client);
  res.render('freelancers/index', {
    freelancers,
    searchPerformed: false // 👈 Ajoute ceci ici aussi
  });
});





// Formulaire d'ajout
app.get('/freelancer/create', (req, res) => {
  res.render('freelancers/form');
});

// Traitement du formulaire

app.post('/freelancer/create', async (req, res) => {
  const { nom, email, bio, base64Image } = req.body;
  await createFreelancer(client, { nom, email, bio, base64Image });
  res.redirect('/');
});

// Afficher 1 freelancer
app.get('/freelancer/:id', async (req, res) => {
  const freelancer = await getFreelancerById(client, req.params.id);
  res.render('freelancers/show', { freelancer });
});






app.get('/search', async (req, res) => {
  const { skill } = req.query;

  if (!skill) {
    const freelancers = await getFreelancers(client);
    return res.render('freelancers/index', {
      freelancers,
      searchPerformed: false
    });
  }

  try {
    const matchedFreelancers = await getFreelancersBySkill(client, skill) || [];
    const allFreelancers = await getFreelancers(client);

    res.render('freelancers/index', {
      freelancers: allFreelancers,
      searchPerformed: true,
      matchedFreelancers,
      searchSkill: skill
    });
  } catch (error) {
    console.error(error);
    res.status(500).send("Erreur lors de la recherche par compétence");
  }
});







// Ajouter une compétence
app.post('/freelancers/:id/addSkill', async (req, res) => {
  const freelancerId = req.params.id;
  const { nom } = req.body;

  try {
    await addSkill(client, { freelancerId, nom });
    res.redirect(`/freelancer/${freelancerId}`);
  } catch (error) {
    console.error(error);
    res.status(500).send("Erreur lors de l'ajout de la compétence");
  }
});

// Ajouter un lien social
app.post('/freelancers/:id/addSocialLink', async (req, res) => {
  const freelancerId = req.params.id;
  const { type, url } = req.body;

  try {
    await addSocialLink(client, freelancerId, url, type); // ✅ CORRECTION ICI
    res.redirect(`/freelancer/${freelancerId}`);
  } catch (error) {
    console.error(error.response?.errors || error.message); // pour voir l’erreur GraphQL
    res.status(500).send("Erreur lors de l'ajout du lien social");
  }
});








// Supprimer une compétence
app.delete('/freelancers/:freelancerId/skills/:skillId', async (req, res) => {
  const { freelancerId, skillId } = req.params;

  try {
    await removeSkill(client, freelancerId, skillId); // utilise ta fonction existante
    res.redirect(`/freelancer/${freelancerId}`); // 👈 pas `freelancers/`
  } catch (error) {
    console.error(error);
    res.status(500).send("Erreur lors de la suppression de la compétence");
  }
});




app.delete('/freelancers/:freelancerId/links/:linkId', async (req, res) => {
  const { freelancerId, linkId } = req.params;

  try {
    await removeSocialLink(client, freelancerId, linkId);
    res.redirect(`/freelancer/${freelancerId}`);
  } catch (error) {
    console.error(error);
    res.status(500).send("Erreur lors de la suppression du lien social");
  }
});






















app.get('/freelancer/edit/:id', async (req, res) => {
  try {
    const freelancer = await getFreelancerById(client, req.params.id);
    res.render('freelancers/edit', { freelancer });
  } catch (error) {
    console.error(error);
    res.status(500).send("Erreur lors du chargement de la page d'édition");
  }
});






app.post('/freelancer/update', async (req, res) => {
  const { id, nom, email, bio } = req.body;

  // Récupération des tableaux depuis le formulaire
  let { skills, urls, types } = req.body;

  // Si un seul élément (non-array), le forcer en tableau
  skills = typeof skills === 'string' ? [skills] : skills;
  urls = typeof urls === 'string' ? [urls] : urls;
  types = typeof types === 'string' ? [types] : types;

  try {
    await updateFreelancer(client, {
      id,
      nom,
      email,
      bio,
      skills,
      urls,
      types
    });

    res.redirect(`/freelancer/${id}`);
  } catch (error) {
    console.error(error);
    res.status(500).send("Erreur lors de la mise à jour du freelancer");
  }
});










app.listen(PORT, () => console.log(`Client running on http://localhost:${PORT}`));
