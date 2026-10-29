package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.web.dto.MaintenanceRequest;

public interface IMaintenanceService {
    List<Maintenance> findAll();
    Maintenance findById(Long id);
    Maintenance create(MaintenanceRequest request);
    Maintenance update(Long id, MaintenanceRequest request);
    void deleteById(Long id);
}
