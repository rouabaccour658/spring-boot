package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.ContratCreateRequest;
import tn.esprit.autoloc.web.dto.PaiementRequest;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.domain.Paiement;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private static final String CONTRAT_ENTITY_NAME = "Contrat";
    private static final String RESERVATION_ENTITY_NAME = "Reservation";

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    @Transactional
    public ContratDTO create(ContratDTO contratDto) {
        if (contratDto == null) {
            throw new IllegalArgumentException("Le contrat est obligatoire");
        }
        if (contratDto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        valider(contratDto);
        Contrat contrat = contratMapper.toEntity(contratDto);
        contrat.setPaiements(new HashSet<>());
        lierReservation(contrat, contratDto.getReservationId(), null);
        return contratMapper.toDto(contratRepository.save(contrat));
    }

    @Override
    @Transactional
    public ContratDTO create(ContratCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Le contrat est obligatoire");
        }
        Contrat contrat = new Contrat();
        contrat.setDateSignature(request.dateSignature());
        contrat.setMontantTotal(request.montantTotal());
        if (request.paiements() != null) {
            for (PaiementRequest paiementRequest : request.paiements()) {
                if (paiementRequest == null) {
                    throw new IllegalArgumentException("Un paiement ne peut pas être nul");
                }
                Paiement paiement = new Paiement();
                paiement.setMontant(paiementRequest.montant());
                paiement.setDatePaiement(paiementRequest.datePaiement());
                paiement.setModePaiement(paiementRequest.modePaiement());
                paiement.setContrat(contrat);
                contrat.getPaiements().add(paiement);
            }
        }
        if (contrat.getMontantTotal() != null && contrat.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
        return contratMapper.toDto(contratRepository.save(contrat));
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO findById(Long id) {
        return contratMapper.toDto(contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONTRAT_ENTITY_NAME, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> findAll() {
        return contratRepository.findAll().stream().map(contratMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ContratDTO update(Long id, ContratDTO contratDto) {
        if (contratDto == null) {
            throw new IllegalArgumentException("Le contrat est obligatoire");
        }
        if (contratDto.getId() != null && !contratDto.getId().equals(id)) {
            throw new IllegalArgumentException(
                    "L'identifiant du contrat ne correspond pas à celui de l'URL");
        }
        valider(contratDto);
        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONTRAT_ENTITY_NAME, id));
        lierReservation(existant, contratDto.getReservationId(), id);
        Contrat modifications = contratMapper.toEntity(contratDto);
        existant.setDateSignature(modifications.getDateSignature());
        existant.setMontantTotal(modifications.getMontantTotal());
        existant.setValide(modifications.isValide());
        return contratMapper.toDto(contratRepository.save(existant));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONTRAT_ENTITY_NAME, id));

        if (contrat.getReservation() != null) {
            contrat.getReservation().setContrat(null);
            contrat.setReservation(null);
        }

        contrat.getPaiements().clear();
        contratRepository.delete(contrat);
    }

    private void valider(ContratDTO contrat) {
        if (contrat.getMontantTotal() != null
                && contrat.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
    }

    private void lierReservation(Contrat contrat, Long reservationId, Long contratId) {
        Reservation actuelle = contrat.getReservation();
        if (reservationId == null) {
            if (actuelle != null) {
                actuelle.setContrat(null);
                contrat.setReservation(null);
            }
            return;
        }
        Reservation nouvelle = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_ENTITY_NAME, reservationId));
        if (nouvelle.getContrat() != null
                && !Objects.equals(nouvelle.getContrat().getIdContrat(), contratId)) {
            throw new IllegalArgumentException("Cette réservation est déjà associée à un contrat");
        }
        if (actuelle != null && !Objects.equals(actuelle.getIdReservation(), reservationId)) {
            actuelle.setContrat(null);
        }
        nouvelle.setContrat(contrat);
        contrat.setReservation(nouvelle);
    }
}