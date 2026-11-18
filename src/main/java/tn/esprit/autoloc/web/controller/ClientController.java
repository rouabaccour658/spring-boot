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
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientDTO;

@Tag(name = "Clients", description = "Gestion des clients")
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService clientService;

    @Operation(summary = "Lister les clients")
    @ApiResponse(responseCode = "200", description = "Liste des clients retournée")
    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @Operation(summary = "Consulter un client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client retourné"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(summary = "Créer un client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé"),
            @ApiResponse(responseCode = "400", description = "Nom ou prénom invalide")
    })
    @PostMapping
    public ResponseEntity<ClientDTO> create(@Valid @RequestBody ClientDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(request));
    }

    @Operation(summary = "Modifier un client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client modifié"),
            @ApiResponse(responseCode = "400", description = "Nom ou prénom invalide"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(@PathVariable Long id, @Valid @RequestBody ClientDTO request) {
        return ResponseEntity.ok(clientService.update(id, request));
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client supprimé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
