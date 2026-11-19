package tn.esprit.autoloc.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;

import java.time.LocalDate;

@Component
public class ReservationRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ReservationRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReservationRequestDTO request = (ReservationRequestDTO) target;
        LocalDate aujourdHui = LocalDate.now();

        if (request.getDateDebut() != null && request.getDateDebut().isBefore(aujourdHui)) {
            errors.rejectValue("dateDebut", "reservation.dateDebut.past",
                    "La date de début ne peut pas être antérieure à aujourd'hui");
        }
        if (request.getDateDebut() != null && request.getDateFin() != null
                && !request.getDateFin().isAfter(request.getDateDebut())) {
            errors.rejectValue("dateFin", "reservation.dateFin.invalid",
                    "La date de fin doit être strictement postérieure à la date de début");
        }
    }
}
