package tn.esprit.autoloc.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeRequest;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    @Transactional
    public Vehicule create(VehiculeRequest request) {
        valider(request);
        Vehicule vehicule = new Vehicule();
        appliquer(vehicule, request);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule update(Long id, VehiculeRequest request) {
        valider(request);
        Vehicule vehicule = findById(id);
        appliquer(vehicule, request);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        vehiculeRepository.delete(findById(id));
    }

    private void appliquer(Vehicule vehicule, VehiculeRequest request) {
        vehicule.setImmatriculation(request.immatriculation().trim());
        vehicule.setMarque(request.marque().trim());
        vehicule.setModele(request.modele().trim());
        vehicule.setCategorie(request.categorie());
        vehicule.setTarifJournalier(request.tarifJournalier());
        vehicule.setStatut(request.statut());
        vehicule.setAgence(request.agenceId() == null ? null : agenceRepository.findById(request.agenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", request.agenceId())));

        Set<Long> ids = request.equipementIds() == null ? Set.of() : request.equipementIds();
        if (ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Un identifiant d'équipement ne peut pas être nul");
        }
        Set<Equipement> equipements = ids.stream()
                .map(id -> equipementRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Equipement", id)))
                .collect(Collectors.toCollection(HashSet::new));
        vehicule.setEquipements(equipements);
    }

    private void valider(VehiculeRequest request) {
        if (request == null || request.immatriculation() == null || request.immatriculation().isBlank()
                || request.marque() == null || request.marque().isBlank()
                || request.modele() == null || request.modele().isBlank()
                || request.categorie() == null || request.statut() == null
                || request.tarifJournalier() == null
                || request.tarifJournalier().signum() < 0) {
            throw new IllegalArgumentException("Les champs obligatoires du véhicule sont invalides");
        }
    }
}
