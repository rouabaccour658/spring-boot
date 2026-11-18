package tn.esprit.autoloc.web.controller;

import java.util.List;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratCreateRequest;
import tn.esprit.autoloc.web.dto.ContratDTO;

@Tag(name = "Contrats", description = "Gestion des contrats de location")
@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContratController {

    private final IContratService contratService;

    @Operation(summary = "Lister les contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats retournée")
    @GetMapping
    public ResponseEntity<List<ContratDTO>> findAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @Operation(summary = "Consulter un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat retourné"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContratDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @Operation(summary = "Créer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé"),
            @ApiResponse(responseCode = "400", description = "Données du contrat invalides")
    })
    @PostMapping
    public ResponseEntity<ContratDTO> create(@Valid @RequestBody ContratDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.create(request));
    }

    @PostMapping("/avec-paiements")
    public ResponseEntity<ContratDTO> createAvecPaiements(@Valid @RequestBody ContratCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.create(request));
    }

    @Operation(summary = "Modifier un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié"),
            @ApiResponse(responseCode = "400", description = "Données du contrat invalides"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContratDTO> update(@PathVariable Long id, @Valid @RequestBody ContratDTO request) {
        return ResponseEntity.ok(contratService.update(id, request));
    }

    @Operation(summary = "Supprimer un contrat")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Contrat et paiements associés supprimés"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
