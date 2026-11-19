package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.service.observer.ReservationSubject;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationSubject reservationSubject;

    public Reservation creerReservation(Client client) {
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .statut(StatutReservation.CONFIRMEE)
                .build();
        reservationSubject.notifierConfirmation(reservation);
        return reservation;
    }

    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.ANNULEE);
        log.info("Reservation #{} annulee", reservation.getIdReservation());
        reservationSubject.notifierAnnulation(reservation);
        return reservation;
    }
}
