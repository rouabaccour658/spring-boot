package tn.esprit.autoloc.web.controller;

import java.util.List;
import jakarta.validation.Valid;
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
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDTO;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;

    @GetMapping
    public ResponseEntity<List<AgenceDTO>> findAll() {
        return ResponseEntity.ok(agenceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenceDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(agenceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AgenceDTO> create(@Valid @RequestBody AgenceDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agenceService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgenceDTO> update(@PathVariable Long id, @Valid @RequestBody AgenceDTO request) {
        return ResponseEntity.ok(agenceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        agenceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
