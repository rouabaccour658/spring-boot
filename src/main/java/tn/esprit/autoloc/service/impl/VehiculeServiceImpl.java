package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> findAll() {
        return vehiculeRepository.findAll().stream().map(vehiculeMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDTO findById(Long id) {
        return vehiculeMapper.toDto(vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id)));
    }

    @Override
    @Transactional
    public VehiculeDTO create(VehiculeDTO request) {
        valider(request);
        if (request.getId() != null) {
            throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        Vehicule vehicule = vehiculeMapper.toEntity(request);
        associerAgence(vehicule, request.getAgenceId());
        return vehiculeMapper.toDto(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional
    public VehiculeDTO update(Long id, VehiculeDTO request) {
        valider(request);
        if (request.getId() != null && !request.getId().equals(id)) {
            throw new IllegalArgumentException("L'identifiant du véhicule ne correspond pas à l'URL");
        }
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
        Vehicule modifications = vehiculeMapper.toEntity(request);
        vehicule.setImmatriculation(modifications.getImmatriculation().trim());
        vehicule.setMarque(modifications.getMarque().trim());
        vehicule.setModele(modifications.getModele().trim());
        vehicule.setCategorie(modifications.getCategorie());
        vehicule.setTarifJournalier(modifications.getTarifJournalier());
        vehicule.setStatut(modifications.getStatut());
        associerAgence(vehicule, request.getAgenceId());
        return vehiculeMapper.toDto(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
        vehicule.getReservations().forEach(reservation -> {
            if (reservation.getClient() != null) {
                reservation.getClient().getReservations().remove(reservation);
            }
            if (reservation.getContrat() != null) {
                reservation.getContrat().setReservation(null);
                reservation.setContrat(null);
            }
        });
        if (vehicule.getAgence() != null) {
            vehicule.getAgence().getVehicules().remove(vehicule);
        }
        vehicule.getEquipements().forEach(equipement -> equipement.getVehicules().remove(vehicule));
        vehicule.getEquipements().clear();
        vehiculeRepository.delete(vehicule);
    }

    @Override
    @Transactional
    public void affecterVehiculeAAgence(Long vehiculeId, Long agenceId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + vehiculeId));
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable, id=" + agenceId));
        Agence ancienneAgence = vehicule.getAgence();
        if (ancienneAgence != null && ancienneAgence != agence) {
            ancienneAgence.getVehicules().remove(vehicule);
        }
        vehicule.setAgence(agence);
        agence.getVehicules().add(vehicule);
        vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public void desaffecterVehiculeDeAgence(Long vehiculeId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + vehiculeId));
        if (vehicule.getAgence() == null) {
            throw new IllegalStateException("Le véhicule n'est affecté à aucune agence");
        }
        vehicule.getAgence().getVehicules().remove(vehicule);
        vehicule.setAgence(null);
        vehiculeRepository.save(vehicule);
    }

    private void associerAgence(Vehicule vehicule, Long agenceId) {
        Agence nouvelleAgence = agenceId == null ? null : agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));
        Agence ancienneAgence = vehicule.getAgence();
        if (ancienneAgence != nouvelleAgence) {
            if (ancienneAgence != null) {
                ancienneAgence.getVehicules().remove(vehicule);
            }
            if (nouvelleAgence != null) {
                nouvelleAgence.getVehicules().add(vehicule);
            }
            vehicule.setAgence(nouvelleAgence);
        }
    }

    private void valider(VehiculeDTO request) {
        if (request == null || request.getImmatriculation() == null || request.getImmatriculation().isBlank()
                || request.getMarque() == null || request.getMarque().isBlank()
                || request.getModele() == null || request.getModele().isBlank()
                || request.getCategorie() == null || request.getStatut() == null
                || request.getTarifJournalier() == null
                || request.getTarifJournalier().signum() < 0) {
            throw new IllegalArgumentException("Les champs obligatoires du véhicule sont invalides");
        }
    }
}
