package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.web.dto.EmployeRequest;

public interface IEmployeService {
    List<Employe> findAll();
    Employe findById(Long id);
    Employe create(EmployeRequest request);
    Employe update(Long id, EmployeRequest request);
    void deleteById(Long id);
}
