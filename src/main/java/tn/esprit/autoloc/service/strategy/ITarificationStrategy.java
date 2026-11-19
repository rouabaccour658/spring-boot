package tn.esprit.autoloc.service.strategy;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;

public interface ITarificationStrategy {
    BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation);
    String getCode();
}