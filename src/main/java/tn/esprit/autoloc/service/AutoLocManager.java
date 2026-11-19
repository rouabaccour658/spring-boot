package tn.esprit.autoloc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoLocManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoLocManager.class);

    public void louerVehicule(String typeReservation, int nombreJours, String clientEmail) {
        double prix = 0;
        if (typeReservation.equals("LongDuree")) {
            prix = nombreJours * 120.0;
        } else if (typeReservation.equals("Weekend")) {
            prix = nombreJours * 180.0;
        } else if (typeReservation.equals("Standard")) {
            prix = nombreJours * 150.0;
        }
        LOGGER.info("Contrat créé");
        LOGGER.info("Prix total : {}", prix);
        envoyerEmail(clientEmail);
        enregistrerPaiement();
        sauvegarderContrat();
    }

    private void envoyerEmail(String email) { LOGGER.info("Email envoyé à {}", email); }
    private void enregistrerPaiement() { LOGGER.info("Paiement enregistré"); }
    private void sauvegarderContrat() { LOGGER.info("Contrat sauvegardé"); }
}