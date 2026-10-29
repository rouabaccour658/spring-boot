package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequest;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IContratRepository contratRepository;

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    @Transactional
    public Reservation create(ReservationRequest request) {
        valider(request);
        Reservation reservation = new Reservation();
        appliquer(reservation, request);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation update(Long id, ReservationRequest request) {
        valider(request);
        Reservation reservation = findById(id);
        Contrat ancienContrat = reservation.getContrat();
        appliquer(reservation, request);
        if (ancienContrat != null && ancienContrat != reservation.getContrat()) {
            ancienContrat.setReservation(null);
        }
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Reservation reservation = findById(id);
        if (reservation.getContrat() != null) {
            reservation.getContrat().setReservation(null);
            reservation.setContrat(null);
        }
        reservationRepository.delete(reservation);
    }

    private void appliquer(Reservation reservation, ReservationRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", request.clientId()));
        Vehicule vehicule = vehiculeRepository.findById(request.vehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", request.vehiculeId()));
        Contrat contrat = request.contratId() == null ? null : contratRepository.findById(request.contratId())
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", request.contratId()));
        if (contrat != null && contrat.getReservation() != null
                && !contrat.getReservation().getIdReservation().equals(reservation.getIdReservation())) {
            throw new IllegalArgumentException("Ce contrat est déjà associé à une réservation");
        }

        Contrat ancienContrat = reservation.getContrat();
        if (ancienContrat != null && ancienContrat != contrat) {
            ancienContrat.setReservation(null);
        }
        reservation.setDateDebut(request.dateDebut());
        reservation.setDateFin(request.dateFin());
        reservation.setStatut(request.statut());
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setContrat(contrat);
        if (contrat != null) {
            contrat.setReservation(reservation);
        }
    }

    private void valider(ReservationRequest request) {
        if (request == null || request.dateDebut() == null || request.dateFin() == null
                || !request.dateFin().isAfter(request.dateDebut())
                || request.statut() == null || request.clientId() == null
                || request.vehiculeId() == null) {
            throw new IllegalArgumentException(
                    "Dates valides, statut, client et véhicule sont obligatoires pour une réservation");
        }
    }
}
