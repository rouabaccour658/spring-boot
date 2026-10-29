package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeRequest;

public interface IVehiculeService {
    List<Vehicule> findAll();
    Vehicule findById(Long id);
    Vehicule create(VehiculeRequest request);
    Vehicule update(Long id, VehiculeRequest request);
    void deleteById(Long id);
}
