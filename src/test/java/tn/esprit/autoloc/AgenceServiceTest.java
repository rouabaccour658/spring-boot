package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AgenceServiceTest {

    @Autowired
    private IAgenceService agenceService;

    @Test
    void testAgenceService() {
        int countAvant = agenceService.findAll().size();

        // 1. Création d'une agence
        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("10 Avenue Habib Bourguiba");
        agence.setTelephone("71000000");

        // 2. Enregistrement + affichage de l'objet retourné
        Agence saved = agenceService.create(agence);
        System.out.println("Agence enregistrée : id=" + saved.getIdAgence()
                + ", nom=" + saved.getNom() + ", ville=" + saved.getVille());
        assertNotNull(saved.getIdAgence());

        // 3. Affichage de toutes les agences
        List<Agence> agences = agenceService.findAll();
        System.out.println("Liste des agences :");
        agences.forEach(a -> System.out.println(" - " + a.getIdAgence() + " : " + a.getNom()));

        // 4. Recherche par id
        Agence trouvee = agenceService.findById(saved.getIdAgence());
        System.out.println("Agence trouvée : " + trouvee.getNom() + " (" + trouvee.getVille() + ")");
        assertEquals("Agence Tunis Centre", trouvee.getNom());

        // 5. Nombre d'agences (pas de count() dans le service : on utilise la taille de la liste)
        int countApres = agenceService.findAll().size();
        System.out.println("Nombre d'agences : " + countApres);
        assertEquals(countAvant + 1, countApres);

        // 6. Suppression, puis vérification qu'elle n'existe plus
        agenceService.deleteById(saved.getIdAgence());
        assertThrows(ResourceNotFoundException.class,
                () -> agenceService.findById(saved.getIdAgence()));
        System.out.println("Agence " + saved.getIdAgence() + " supprimée : introuvable, comme attendu.");
        assertEquals(countAvant, agenceService.findAll().size());
    }
}