package tn.esprit.autoloc.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class EmployeServiceTest {

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IEmployeRepository employeRepository;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void desaffecterEmployeEnregistreAgenceNulleEnBase() {
        Agence agence = new Agence();
        agence.setNom("Agence de test");
        agence.setVille("Tunis");
        agence.setAdresse("Adresse test");
        agence.setTelephone("71000000");
        agence = agenceRepository.saveAndFlush(agence);

        Employe employe = new Employe();
        employe.setNom("Test");
        employe.setPrenom("Employe");
        employe.setRole(RoleEmploye.AGENT);
        employe.setAgence(agence);
        employe = employeRepository.saveAndFlush(employe);
        Long employeId = employe.getIdEmploye();

        employeService.desaffecterEmployeDeAgence(employeId);

        entityManager.clear();
        Employe employeRelu = employeRepository.findById(employeId).orElseThrow();
        assertNull(employeRelu.getAgence());

        employeRepository.delete(employeRelu);
        agenceRepository.delete(agenceRepository.findById(agence.getIdAgence()).orElseThrow());
    }
}
