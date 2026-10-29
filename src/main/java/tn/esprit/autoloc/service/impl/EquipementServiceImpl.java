package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IEquipementService;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    public Equipement findById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }

    @Override
    public Equipement create(Equipement equipement) {
        valider(equipement);
        if (equipement.getIdEquipement() != null) {
            throw new IllegalArgumentException("Un nouvel équipement ne doit pas avoir d'identifiant");
        }
        equipement.setVehicules(new HashSet<>());
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement update(Long id, Equipement equipement) {
        valider(equipement);
        if (equipement.getIdEquipement() != null && !equipement.getIdEquipement().equals(id)) {
            throw new IllegalArgumentException("L'identifiant de l'équipement ne correspond pas à l'URL");
        }
        Equipement existant = findById(id);
        existant.setLibelle(equipement.getLibelle().trim());
        return equipementRepository.save(existant);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Equipement equipement = findById(id);
        equipement.getVehicules().forEach(vehicule -> vehicule.getEquipements().remove(equipement));
        vehiculeRepository.flush();
        equipementRepository.delete(equipement);
    }

    private void valider(Equipement equipement) {
        if (equipement == null || equipement.getLibelle() == null || equipement.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libellé de l'équipement est obligatoire");
        }
    }
}
