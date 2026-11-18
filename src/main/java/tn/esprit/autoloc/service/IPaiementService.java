package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.web.dto.PaiementDTO;

public interface IPaiementService {

    List<PaiementDTO> findAll();

    PaiementDTO findById(Long id);
}
