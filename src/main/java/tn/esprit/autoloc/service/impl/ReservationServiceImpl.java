package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private static final String RESERVATION_ENTITY_NAME = "Reservation";
    private static final String CLIENT_ENTITY_NAME = "Client";
    private static final String VEHICULE_ENTITY_NAME = "Vehicule";

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream().map(reservationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        return reservationMapper.toDto(reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_ENTITY_NAME, id)));
    }

    @Override
    @Transactional
    public ReservationResponseDTO create(ReservationRequestDTO request) {
        valider(request);
        Reservation reservation = reservationMapper.toEntity(request);
        appliquer(reservation, request, reservation, true);
        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponseDTO update(Long id, ReservationRequestDTO request) {
        valider(request);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_ENTITY_NAME, id));
        Reservation modifications = reservationMapper.toEntity(request);
        appliquer(reservation, request, modifications, false);
        return reservationMapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_ENTITY_NAME, id));
        if (reservation.getClient() != null) {
            reservation.getClient().getReservations().remove(reservation);
        }
        if (reservation.getVehicule() != null) {
            reservation.getVehicule().getReservations().remove(reservation);
        }
        if (reservation.getContrat() != null) {
            reservation.getContrat().setReservation(null);
            reservation.setContrat(null);
        }
        reservationRepository.delete(reservation);
    }

    private void appliquer(Reservation reservation, ReservationRequestDTO request,
                           Reservation modifications, boolean nouvelle) {
        Client ancienClient = reservation.getClient();
        Vehicule ancienVehicule = reservation.getVehicule();
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_ENTITY_NAME, request.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException(VEHICULE_ENTITY_NAME, request.getVehiculeId()));

        if (ancienClient == null || !Objects.equals(ancienClient.getIdClient(), client.getIdClient())) {
            if (ancienClient != null) {
                ancienClient.getReservations().remove(reservation);
            }
            client.getReservations().add(reservation);
        }
        if (ancienVehicule == null || !Objects.equals(ancienVehicule.getIdVehicule(), vehicule.getIdVehicule())) {
            if (ancienVehicule != null) {
                ancienVehicule.getReservations().remove(reservation);
            }
            vehicule.getReservations().add(reservation);
        }
        reservation.setDateDebut(modifications.getDateDebut());
        reservation.setDateFin(modifications.getDateFin());
        if (nouvelle) {
            reservation.setStatut(StatutReservation.EN_ATTENTE);
        }
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
    }

    private void valider(ReservationRequestDTO request) {
        if (request == null || request.getDateDebut() == null || request.getDateFin() == null
                || !request.getDateFin().isAfter(request.getDateDebut())
                || request.getClientId() == null || request.getVehiculeId() == null) {
            throw new IllegalArgumentException(
                    "Dates valides, client et véhicule sont obligatoires pour une réservation");
        }
    }
}
