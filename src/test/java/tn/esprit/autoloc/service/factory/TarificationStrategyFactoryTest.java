package tn.esprit.autoloc.service.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.strategy.ITarificationStrategy;
import tn.esprit.autoloc.service.strategy.TarifLongueDureeStrategy;
import tn.esprit.autoloc.service.strategy.TarifStandardStrategy;
import tn.esprit.autoloc.service.strategy.TarifWeekendStrategy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TarificationStrategyFactoryTest {

    private TarificationStrategyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TarificationStrategyFactory(List.of(
                new TarifStandardStrategy(),
                new TarifLongueDureeStrategy(),
                new TarifWeekendStrategy()));
    }

    @Test
    void doitRetournerLongueDureePour7JoursOuPlus() {
        Reservation reservation = reservation("2024-01-01", "2024-01-08");

        ITarificationStrategy strategie = factory.resoudre(reservation);

        assertThat(strategie).isInstanceOf(TarifLongueDureeStrategy.class);
        assertThat(strategie.getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerLongueDureePourExactement7Jours() {
        Reservation reservation = reservation("2024-05-01", "2024-05-08");

        assertThat(factory.resoudre(reservation).getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerWeekendPourCourtSejourVendredi() {
        Reservation reservation = reservation("2024-03-08", "2024-03-10");

        assertThat(factory.resoudre(reservation).getCode()).isEqualTo("WEEKEND");
    }

    @Test
    void doitRetournerStandardPourMoyenneDuree() {
        Reservation reservation = reservation("2024-03-04", "2024-03-08");

        assertThat(factory.resoudre(reservation).getCode()).isEqualTo("STANDARD");
    }

    private Reservation reservation(String dateDebut, String dateFin) {
        return Reservation.builder()
                .dateDebut(LocalDate.parse(dateDebut))
                .dateFin(LocalDate.parse(dateFin))
                .build();
    }
}
