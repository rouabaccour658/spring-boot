package tn.esprit.autoloc.web.dto;

import java.time.LocalDate;

public record MaintenanceRequest(
        LocalDate dateDebut,
        LocalDate dateFin,
        String description,
        Long vehiculeId) {
}
