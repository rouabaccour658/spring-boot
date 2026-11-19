package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDTO;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private static final String AGENCE_ENTITY_NAME = "Agence";

    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    @Override
    @Transactional
    public AgenceDTO create(AgenceDTO agence) {
        if (agence == null) {
            throw new IllegalArgumentException("L'agence est obligatoire");
        }
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        return agenceMapper.toDto(agenceRepository.save(agenceMapper.toEntity(agence)));
    }

    @Override
    @Transactional(readOnly = true)
    public AgenceDTO findById(Long id) {
        return agenceMapper.toDto(agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AGENCE_ENTITY_NAME, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenceDTO> findAll() {
        return agenceRepository.findAll().stream().map(agenceMapper::toDto).toList();
    }

    @Override
    @Transactional
    public AgenceDTO update(Long id, AgenceDTO agence) {
        if (agence == null) {
            throw new IllegalArgumentException("L'agence est obligatoire");
        }
        if (agence.getIdAgence() != null && !agence.getIdAgence().equals(id)) {
            throw new IllegalArgumentException(
                    "L'identifiant de l'agence ne correspond pas à celui de l'URL");
        }
        Agence existante = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AGENCE_ENTITY_NAME, id));
        Agence modifications = agenceMapper.toEntity(agence);
        existante.setNom(modifications.getNom());
        existante.setVille(modifications.getVille());
        existante.setAdresse(modifications.getAdresse());
        existante.setTelephone(modifications.getTelephone());
        return agenceMapper.toDto(agenceRepository.save(existante));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AGENCE_ENTITY_NAME, id));
        for (Vehicule vehicule : agence.getVehicules()) {
            vehicule.getEquipements().forEach(equipement -> equipement.getVehicules().remove(vehicule));
            for (Reservation reservation : vehicule.getReservations()) {
                if (reservation.getClient() != null) {
                    reservation.getClient().getReservations().remove(reservation);
                }
                if (reservation.getContrat() != null) {
                    reservation.getContrat().setReservation(null);
                    reservation.setContrat(null);
                }
            }
        }
        agenceRepository.delete(agence);
    }
}
