package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;
import java.util.Set;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;

public record VehiculeRequest(
        String immatriculation,
        String marque,
        String modele,
        CategorieVehicule categorie,
        BigDecimal tarifJournalier,
        StatutVehicule statut,
        Long agenceId,
        Set<Long> equipementIds) {
}
