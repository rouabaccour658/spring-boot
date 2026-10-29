package tn.esprit.autoloc.web.dto;

public record ClientRequest(
        String nom,
        String prenom,
        String email,
        String telephone,
        String numPermis) {
}
