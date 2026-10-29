package tn.esprit.autoloc.web.dto;

import tn.esprit.autoloc.domain.enums.RoleEmploye;

public record EmployeRequest(String nom, String prenom, RoleEmploye role, Long agenceId) {
}
