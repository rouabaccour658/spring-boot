package tn.esprit.autoloc.web.controller;

import java.util.List;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

@Tag(name = "Véhicules", description = "Gestion des véhicules")
@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules retournée")
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> findAll() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @Operation(summary = "Consulter un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule retourné"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @Operation(summary = "Créer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @PostMapping
    public ResponseEntity<VehiculeDTO> create(@Valid @RequestBody VehiculeDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(request));
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Véhicule ou agence introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> update(@PathVariable Long id, @Valid @RequestBody VehiculeDTO request) {
        return ResponseEntity.ok(vehiculeService.update(id, request));
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{vehiculeId}/agence/{agenceId}")
    public ResponseEntity<Void> affecterVehiculeAAgence(@PathVariable Long vehiculeId,
                                                        @PathVariable Long agenceId) {
        vehiculeService.affecterVehiculeAAgence(vehiculeId, agenceId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{vehiculeId}/desaffecter")
    public ResponseEntity<Void> desaffecterVehiculeDeAgence(@PathVariable Long vehiculeId) {
        vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);
        return ResponseEntity.noContent().build();
    }
}
