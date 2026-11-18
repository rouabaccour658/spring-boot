package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceRequest;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    @Transactional
    public Maintenance create(MaintenanceRequest request) {
        valider(request);
        Maintenance maintenance = new Maintenance();
        appliquer(maintenance, request);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional
    public Maintenance update(Long id, MaintenanceRequest request) {
        valider(request);
        Maintenance maintenance = findById(id);
        appliquer(maintenance, request);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Maintenance maintenance = findById(id);
        if (maintenance.getVehicule() != null) {
            maintenance.getVehicule().getMaintenances().remove(maintenance);
        }
        maintenanceRepository.delete(maintenance);
    }

    private void appliquer(Maintenance maintenance, MaintenanceRequest request) {
        Vehicule vehicule = vehiculeRepository.findById(request.vehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", request.vehiculeId()));
        Vehicule ancienVehicule = maintenance.getVehicule();
        if (ancienVehicule != vehicule) {
            if (ancienVehicule != null) {
                ancienVehicule.getMaintenances().remove(maintenance);
            }
            vehicule.getMaintenances().add(maintenance);
            maintenance.setVehicule(vehicule);
        }
        maintenance.setDateDebut(request.dateDebut());
        maintenance.setDateFin(request.dateFin());
        maintenance.setDescription(request.description());
    }

    private void valider(MaintenanceRequest request) {
        if (request == null || request.dateDebut() == null || request.dateFin() == null
                || !request.dateFin().isAfter(request.dateDebut()) || request.vehiculeId() == null
                || request.description() == null || request.description().isBlank()) {
            throw new IllegalArgumentException(
                    "Dates valides, description et véhicule sont obligatoires pour une maintenance");
        }
    }
}
