package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeRequest;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    @Transactional
    public Employe create(EmployeRequest request) {
        valider(request);
        Employe employe = new Employe();
        appliquer(employe, request);
        return employeRepository.save(employe);
    }

    @Override
    @Transactional
    public Employe update(Long id, EmployeRequest request) {
        valider(request);
        Employe employe = findById(id);
        appliquer(employe, request);
        return employeRepository.save(employe);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Employe employe = findById(id);
        if (employe.getAgence() != null) {
            employe.getAgence().getEmployes().remove(employe);
        }
        employeRepository.delete(employe);
    }

    @Override
    @Transactional
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable, id=" + employeId));
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable, id=" + agenceId));
        Agence ancienneAgence = employe.getAgence();
        if (ancienneAgence != null && !memesAgences(ancienneAgence, agence)) {
            if (ancienneAgence.getEmployes() != null) {
                ancienneAgence.getEmployes().remove(employe);
            }
        }
        employe.setAgence(agence);
        if (agence.getEmployes() != null) {
            agence.getEmployes().add(employe);
        }
        employeRepository.save(employe);
    }

    @Override
    @Transactional
    public void desaffecterEmployeDeAgence(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable, id=" + employeId));
        if (employe.getAgence() == null) {
            throw new IllegalStateException("L'employé n'est affecté à aucune agence");
        }
        if (employe.getAgence().getEmployes() != null) {
            employe.getAgence().getEmployes().remove(employe);
        }
        employe.setAgence(null);
        employeRepository.saveAndFlush(employe);
    }

    private void appliquer(Employe employe, EmployeRequest request) {
        employe.setNom(request.nom().trim());
        employe.setPrenom(request.prenom().trim());
        employe.setRole(request.role());
        Agence nouvelleAgence =
                request.agenceId() == null ? null : agenceRepository.findById(request.agenceId())
                        .orElseThrow(() -> new ResourceNotFoundException("Agence", request.agenceId()));
        Agence ancienneAgence = employe.getAgence();
        if (!memesAgences(ancienneAgence, nouvelleAgence)) {
            if (ancienneAgence != null) {
                if (ancienneAgence.getEmployes() != null) {
                    ancienneAgence.getEmployes().remove(employe);
                }
            }
            if (nouvelleAgence != null && nouvelleAgence.getEmployes() != null) {
                nouvelleAgence.getEmployes().add(employe);
            }
            employe.setAgence(nouvelleAgence);
        } else if (nouvelleAgence != null) {
            if (nouvelleAgence.getEmployes() != null) {
                nouvelleAgence.getEmployes().add(employe);
            }
            employe.setAgence(nouvelleAgence);
        }
    }

    private boolean memesAgences(Agence premiere, Agence seconde) {
        if (premiere == null || seconde == null) {
            return premiere == null && seconde == null;
        }
        return Objects.equals(premiere.getIdAgence(), seconde.getIdAgence());
    }

    private void valider(EmployeRequest request) {
        if (request == null || request.nom() == null || request.nom().isBlank()
                || request.prenom() == null || request.prenom().isBlank()
                || request.role() == null) {
            throw new IllegalArgumentException("Nom, prénom et rôle de l'employé sont obligatoires");
        }
    }
}
