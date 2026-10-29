package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    public Employe create(EmployeRequest request) {
        valider(request);
        Employe employe = new Employe();
        appliquer(employe, request);
        return employeRepository.save(employe);
    }

    @Override
    public Employe update(Long id, EmployeRequest request) {
        valider(request);
        Employe employe = findById(id);
        appliquer(employe, request);
        return employeRepository.save(employe);
    }

    @Override
    public void deleteById(Long id) {
        employeRepository.delete(findById(id));
    }

    private void appliquer(Employe employe, EmployeRequest request) {
        employe.setNom(request.nom().trim());
        employe.setPrenom(request.prenom().trim());
        employe.setRole(request.role());
        employe.setAgence(request.agenceId() == null ? null : agenceRepository.findById(request.agenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", request.agenceId())));
    }

    private void valider(EmployeRequest request) {
        if (request == null || request.nom() == null || request.nom().isBlank()
                || request.prenom() == null || request.prenom().isBlank()
                || request.role() == null) {
            throw new IllegalArgumentException("Nom, prénom et rôle de l'employé sont obligatoires");
        }
    }
}
