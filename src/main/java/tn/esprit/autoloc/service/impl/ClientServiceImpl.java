package tn.esprit.autoloc.service.impl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientDTO;
import tn.esprit.autoloc.web.mapper.ClientMapper;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream().map(clientMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        return clientMapper.toDto(clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id)));
    }

    @Override
    @Transactional
    public ClientDTO create(ClientDTO clientDto) {
        valider(clientDto);
        if (clientDto.getId() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        Client client = clientMapper.toEntity(clientDto);
        if (client.getDateInscription() == null) {
            client.setDateInscription(LocalDate.now());
        }
        client.setReservations(new HashSet<>());
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDto) {
        valider(clientDto);
        if (clientDto.getId() != null && !clientDto.getId().equals(id)) {
            throw new IllegalArgumentException("L'identifiant du client ne correspond pas à l'URL");
        }
        Client existant = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        Client client = clientMapper.toEntity(clientDto);
        existant.setNom(client.getNom().trim());
        existant.setPrenom(client.getPrenom().trim());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        return clientMapper.toDto(clientRepository.save(existant));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        client.getReservations().forEach(reservation -> {
            if (reservation.getVehicule() != null) {
                reservation.getVehicule().getReservations().remove(reservation);
            }
            if (reservation.getContrat() != null) {
                reservation.getContrat().setReservation(null);
                reservation.setContrat(null);
            }
        });
        clientRepository.delete(client);
    }

    private void valider(ClientDTO client) {
        if (client == null || client.getNom() == null || client.getNom().isBlank()
                || client.getPrenom() == null || client.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le nom et le prénom du client sont obligatoires");
        }
    }
}
