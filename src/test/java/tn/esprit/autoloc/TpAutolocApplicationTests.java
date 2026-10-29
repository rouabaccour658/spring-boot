package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TpAutolocApplicationTests {

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    private IEquipementRepository equipementRepository;

    @Autowired
    private IContratRepository contratRepository;

    @Autowired
    private IMaintenanceRepository maintenanceRepository;

    @Autowired
    private IPaiementRepository paiementRepository;

    @Autowired
    private IContratService contratService;

    @Test
    void contextLoads() {
        // Vérifie uniquement que le contexte Spring démarre
    }

    // ===================== Partie 2 : Agence =====================
    @Test
    void testAgenceRepository() {
        long countAvant = agenceRepository.count();

        Agence agence = new Agence();
        agence.setNom("AutoLoc Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("12 Avenue Habib Bourguiba");
        agence.setTelephone("71123456");

        Agence saved = agenceRepository.save(agence);
        assertNotNull(saved.getIdAgence());
        afficherAgence("Agence enregistrée (save)", saved);

        List<Agence> agences = agenceRepository.findAll();
        System.out.println("=== Liste des agences (findAll) ===");
        agences.forEach(a -> afficherAgence("Agence", a));
        assertFalse(agences.isEmpty());

        Optional<Agence> trouvee = agenceRepository.findById(saved.getIdAgence());
        assertTrue(trouvee.isPresent());
        afficherAgence("Agence trouvée (findById)", trouvee.get());
        assertEquals("AutoLoc Tunis Centre", trouvee.get().getNom());

        boolean existe = agenceRepository.existsById(saved.getIdAgence());
        System.out.println("existsById(" + saved.getIdAgence() + ") = " + existe);
        assertTrue(existe);

        long countApres = agenceRepository.count();
        System.out.println("Nombre d'agences (count) = " + countApres);
        assertEquals(countAvant + 1, countApres);

        agenceRepository.deleteById(saved.getIdAgence());
        boolean existeEncore = agenceRepository.existsById(saved.getIdAgence());
        System.out.println("Après deleteById, existsById = " + existeEncore);
        assertFalse(existeEncore);
        assertTrue(agenceRepository.findById(saved.getIdAgence()).isEmpty());
        assertEquals(countAvant, agenceRepository.count());
    }

    // ===================== Partie 3 : Client =====================
    @Test
    void testClientRepository() {
        long countAvant = clientRepository.count();

        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami.benali@mail.com");
        c1.setTelephone("20111222");
        c1.setNumPermis("TN-123456");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Leila");
        c2.setEmail("leila.trabelsi@mail.com");
        c2.setTelephone("22333444");
        c2.setNumPermis("TN-654321");
        c2.setDateInscription(LocalDate.now());

        Client s1 = clientRepository.save(c1);
        Client s2 = clientRepository.save(c2);
        assertNotNull(s1.getIdClient());
        assertNotNull(s2.getIdClient());
        assertNotEquals(s1.getIdClient(), s2.getIdClient());

        System.out.println("=== Liste des clients (findAll) ===");
        List<Client> clients = clientRepository.findAll();
        clients.forEach(c -> System.out.println("Client -> id=" + c.getIdClient()
                + ", nom=" + c.getNom()
                + ", prenom=" + c.getPrenom()
                + ", email=" + c.getEmail()));
        assertFalse(clients.isEmpty());

        Optional<Client> trouve = clientRepository.findById(s1.getIdClient());
        assertTrue(trouve.isPresent());
        System.out.println("Client trouvé (findById) -> " + trouve.get().getNom());
        assertEquals("Ben Ali", trouve.get().getNom());

        assertTrue(clientRepository.existsById(s1.getIdClient()));
        assertFalse(clientRepository.existsById(999999L));

        long countApres = clientRepository.count();
        System.out.println("Nombre de clients (count) = " + countApres);
        assertEquals(countAvant + 2, countApres);
    }

    // ===================== Partie 4 : Véhicule =====================
    @Test
    void testVehiculeRepository() {
        long countAvant = vehiculeRepository.count();

        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setTarifJournalier(new BigDecimal("80.00"));

        Vehicule saved = vehiculeRepository.save(v);
        assertNotNull(saved.getIdVehicule());
        System.out.println("Véhicule enregistré (save) -> id=" + saved.getIdVehicule()
                + ", marque=" + saved.getMarque()
                + ", tarif=" + saved.getTarifJournalier());

        System.out.println("=== Liste des véhicules (findAll) ===");
        vehiculeRepository.findAll().forEach(x -> System.out.println("Véhicule -> id="
                + x.getIdVehicule()
                + ", marque=" + x.getMarque()
                + ", modele=" + x.getModele()));

        long countApres = vehiculeRepository.count();
        System.out.println("Nombre de véhicules (count) = " + countApres);
        assertEquals(countAvant + 1, countApres);

        // Modification d'une propriété puis save()
        saved.setTarifJournalier(new BigDecimal("95.00"));
        Vehicule modifie = vehiculeRepository.save(saved);
        Optional<Vehicule> relu = vehiculeRepository.findById(modifie.getIdVehicule());
        assertTrue(relu.isPresent());
        System.out.println("Tarif après modification = " + relu.get().getTarifJournalier());
        assertEquals(0, new BigDecimal("95.00").compareTo(relu.get().getTarifJournalier()));
        assertEquals(countApres, vehiculeRepository.count());

        // Suppression
        vehiculeRepository.deleteById(saved.getIdVehicule());
        assertFalse(vehiculeRepository.existsById(saved.getIdVehicule()));
        assertEquals(countAvant, vehiculeRepository.count());
    }

    // ===================== Partie 5 : exercice autonome =====================
    @Test
    void testEquipementRepository() {
        Equipement e = new Equipement();
        e.setLibelle("GPS");

        Equipement saved = equipementRepository.save(e);
        assertNotNull(saved.getIdEquipement());
        System.out.println("Equipement enregistré (save) -> id=" + saved.getIdEquipement()
                + ", libelle=" + saved.getLibelle());

        System.out.println("=== Liste des équipements (findAll) ===");
        equipementRepository.findAll().forEach(x -> System.out.println("Equipement -> id="
                + x.getIdEquipement() + ", libelle=" + x.getLibelle()));
        assertTrue(equipementRepository.existsById(saved.getIdEquipement()));

        equipementRepository.deleteById(saved.getIdEquipement());
        assertFalse(equipementRepository.existsById(saved.getIdEquipement()));
    }

    @Test
    void testContratRepository() {
        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("450.00"));
        c.setValide(true);

        Contrat saved = contratRepository.save(c);
        assertNotNull(saved.getIdContrat());
        System.out.println("Contrat enregistré (save) -> id=" + saved.getIdContrat()
                + ", montant=" + saved.getMontantTotal()
                + ", valide=" + saved.isValide());

        Optional<Contrat> trouve = contratRepository.findById(saved.getIdContrat());
        assertTrue(trouve.isPresent());
        System.out.println("Contrat trouvé (findById) -> id=" + trouve.get().getIdContrat());
        assertTrue(trouve.get().isValide());
        assertTrue(contratRepository.existsById(saved.getIdContrat()));

        contratRepository.deleteById(saved.getIdContrat());
        assertFalse(contratRepository.existsById(saved.getIdContrat()));
    }

    @Test
    void supprimerContratSupprimeSesPaiements() {
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("450.00"));

        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("100.00"));
        paiement.setDatePaiement(LocalDate.now());
        contrat.getPaiements().add(paiement);
        paiement.setContrat(contrat);

        Contrat saved = contratRepository.save(contrat);
        Long contratId = saved.getIdContrat();
        Long paiementId = saved.getPaiements().iterator().next().getIdPaiement();

        contratService.deleteById(contratId);

        assertFalse(contratRepository.existsById(contratId));
        assertFalse(paiementRepository.existsById(paiementId));
    }

    @Test
    void testMaintenanceRepository() {
        Maintenance m = new Maintenance();
        m.setDateDebut(LocalDate.now());
        m.setDateFin(LocalDate.now().plusDays(2));
        m.setDescription("Vidange et contrôle des freins");

        Maintenance saved = maintenanceRepository.save(m);
        assertNotNull(saved.getIdMaintenance());
        System.out.println("Maintenance enregistrée (save) -> id=" + saved.getIdMaintenance()
                + ", description=" + saved.getDescription());

        Optional<Maintenance> trouvee = maintenanceRepository.findById(saved.getIdMaintenance());
        assertTrue(trouvee.isPresent());
        System.out.println("Maintenance trouvée (findById) -> id=" + trouvee.get().getIdMaintenance());
        assertEquals("Vidange et contrôle des freins", trouvee.get().getDescription());
        assertTrue(maintenanceRepository.existsById(saved.getIdMaintenance()));

        maintenanceRepository.deleteById(saved.getIdMaintenance());
        assertFalse(maintenanceRepository.existsById(saved.getIdMaintenance()));
    }

    private static void afficherAgence(String titre, Agence a) {
        System.out.println(titre + " -> id=" + a.getIdAgence()
                + ", nom=" + a.getNom()
                + ", ville=" + a.getVille()
                + ", adresse=" + a.getAdresse()
                + ", telephone=" + a.getTelephone());
    }
}