package tn.esprit.autoloc.service.impl;

import java.util.List;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IClientService;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public Client create(Client client) {
        valider(client);
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        client.setReservations(new HashSet<>());
        return clientRepository.save(client);
    }

    @Override
    public Client update(Long id, Client client) {
        valider(client);
        if (client.getIdClient() != null && !client.getIdClient().equals(id)) {
            throw new IllegalArgumentException("L'identifiant du client ne correspond pas à l'URL");
        }
        Client existant = findById(id);
        existant.setNom(client.getNom().trim());
        existant.setPrenom(client.getPrenom().trim());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());
        return clientRepository.save(existant);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        clientRepository.delete(findById(id));
    }

    private void valider(Client client) {
        if (client == null || client.getNom() == null || client.getNom().isBlank()
                || client.getPrenom() == null || client.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le nom et le prénom du client sont obligatoires");
        }
    }
}
