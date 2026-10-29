package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationRequest;

public interface IReservationService {
    List<Reservation> findAll();
    Reservation findById(Long id);
    Reservation create(ReservationRequest request);
    Reservation update(Long id, ReservationRequest request);
    void deleteById(Long id);
}
