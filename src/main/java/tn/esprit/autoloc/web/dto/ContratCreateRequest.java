package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContratCreateRequest(
        LocalDate dateSignature,
        BigDecimal montantTotal,
        List<PaiementRequest> paiements) {
}
