package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

public interface IReservationService {
    List<ReservationResponseDTO> findAll();
    ReservationResponseDTO findById(Long id);
    ReservationResponseDTO create(ReservationRequestDTO request);
    ReservationResponseDTO update(Long id, ReservationRequestDTO request);
    void deleteById(Long id);
}
