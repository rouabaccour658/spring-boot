package tn.esprit.autoloc.web.dto;

public record AgenceRequest(
        String nom,
        String ville,
        String adresse,
        String telephone) {
}
