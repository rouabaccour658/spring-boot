package tn.esprit.autoloc.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import org.springframework.http.HttpStatus;

class ContratControllerTest {

    @Test
    void createReturnsCreatedContractAsDto() {
        IContratService contratService = mock(IContratService.class);
        ContratDTO created = ContratDTO.builder().id(1L).montantTotal(new BigDecimal("450.00")).build();
        when(contratService.create(any(ContratDTO.class))).thenReturn(created);
        ContratController controller = new ContratController(contratService);
        ContratDTO request = ContratDTO.builder().montantTotal(new BigDecimal("450.00")).build();

        var response = controller.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(created, response.getBody());
    }
}
