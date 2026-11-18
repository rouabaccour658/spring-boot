package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

public interface IVehiculeService {
    List<VehiculeDTO> findAll();
    VehiculeDTO findById(Long id);
    VehiculeDTO create(VehiculeDTO request);
    VehiculeDTO update(Long id, VehiculeDTO request);
    void deleteById(Long id);
    void affecterVehiculeAAgence(Long vehiculeId, Long agenceId);
    void desaffecterVehiculeDeAgence(Long vehiculeId);
}
