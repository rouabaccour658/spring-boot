
package tn.esprit.autoloc.web.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementDTO;

@Tag(name = "Paiements", description = "Consultation des paiements de location")
@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final IPaiementService paiementService;

    @Operation(summary = "Lister les paiements")
    @ApiResponse(responseCode = "200", description = "Liste des paiements retournée")
    @GetMapping
    public ResponseEntity<List<PaiementDTO>> findAll() {
        return ResponseEntity.ok(paiementService.findAll());
    }

    @Operation(summary = "Consulter un paiement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement retourné"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaiementDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.findById(id));
    }
}
