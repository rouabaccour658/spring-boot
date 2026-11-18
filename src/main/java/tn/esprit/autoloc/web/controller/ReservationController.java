package tn.esprit.autoloc.web.controller;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.validator.ReservationRequestValidator;

@Tag(name = "Réservations", description = "Gestion des réservations de véhicules")
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;
    private final ReservationRequestValidator reservationRequestValidator;

    @Operation(summary = "Lister les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations retournée")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @Operation(summary = "Consulter une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation retournée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @Operation(summary = "Créer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée"),
            @ApiResponse(responseCode = "400", description = "Données de réservation invalides"),
            @ApiResponse(responseCode = "404", description = "Client ou véhicule introuvable")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReservationRequestDTO request, BindingResult result) {
        reservationRequestValidator.validate(request, result);
        if (result.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(request));
    }

    @Operation(summary = "Modifier une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation modifiée"),
            @ApiResponse(responseCode = "400", description = "Données de réservation invalides"),
            @ApiResponse(responseCode = "404", description = "Réservation, client ou véhicule introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> update(@PathVariable Long id,
                                                         @Valid @RequestBody ReservationRequestDTO request) {
        return ResponseEntity.ok(reservationService.update(id, request));
    }

    @Operation(summary = "Supprimer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réservation supprimée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
