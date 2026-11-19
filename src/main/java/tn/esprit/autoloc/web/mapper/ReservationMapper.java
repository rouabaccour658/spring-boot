package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "idReservation", ignore = true)
    @Mapping(target = "client.idClient", source = "clientId")
    @Mapping(target = "vehicule.idVehicule", source = "vehiculeId")
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "contrat", ignore = true)
    Reservation toEntity(ReservationRequestDTO dto);

    @Mapping(target = "id", source = "idReservation")
    @Mapping(target = "clientId", source = "client.idClient")
    @Mapping(target = "clientNomComplet", expression = "java(nomComplet(reservation))")
    @Mapping(target = "vehiculeId", source = "vehicule.idVehicule")
    @Mapping(target = "vehiculeImmatriculation", source = "vehicule.immatriculation")
    @Mapping(target = "tarifEstime", expression = "java(tarifEstime(reservation))")
    ReservationResponseDTO toDto(Reservation reservation);

    default String nomComplet(Reservation reservation) {
        if (reservation.getClient() == null) {
            return null;
        }
        String prenom = reservation.getClient().getPrenom();
        String nom = reservation.getClient().getNom();
        return prenom == null ? nom : nom == null ? prenom : prenom + " " + nom;
    }

    default BigDecimal tarifEstime(Reservation reservation) {
        if (reservation.getVehicule() == null || reservation.getVehicule().getTarifJournalier() == null
                || reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            return null;
        }
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        return reservation.getVehicule().getTarifJournalier()
                .multiply(BigDecimal.valueOf(jours))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
