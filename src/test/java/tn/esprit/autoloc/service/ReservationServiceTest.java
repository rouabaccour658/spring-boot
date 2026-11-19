package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void doitNotifierLesObservateursLorsDeLaCreation(CapturedOutput output) {
        Client client = new Client();
        client.setEmail("client@example.com");
        client.setTelephone("12345678");

        reservationService.creerReservation(client);

        assertThat(output.getOut()).contains("[EMAIL] Confirmation", "[SMS] Confirmation");
    }

    @Test
    void doitNotifierLesObservateursLorsDeLAnnulation(CapturedOutput output) {
        Client client = new Client();
        client.setEmail("client@example.com");
        client.setTelephone("12345678");
        Reservation reservation = reservationService.creerReservation(client);

        reservationService.annulerReservation(reservation);

        assertThat(output.getOut()).contains("[EMAIL] Annulation", "[SMS] Annulation");
    }
}
