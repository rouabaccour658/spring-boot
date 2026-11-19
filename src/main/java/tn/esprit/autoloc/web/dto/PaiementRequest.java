package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import tn.esprit.autoloc.domain.enums.ModePaiement;

public record PaiementRequest(
        BigDecimal montant,
        LocalDate datePaiement,
        ModePaiement modePaiement) {
}
