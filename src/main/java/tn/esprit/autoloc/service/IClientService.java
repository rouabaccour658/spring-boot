package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.ClientDTO;

public interface IClientService {
    List<ClientDTO> findAll();
    ClientDTO findById(Long id);
    ClientDTO create(ClientDTO client);
    ClientDTO update(Long id, ClientDTO client);
    void deleteById(Long id);
}
