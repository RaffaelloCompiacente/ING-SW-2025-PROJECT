

import dto.InternalTrainQuery;
import dto.TravelSolution;
import model.*;
import org.junit.jupiter.api.Test;
import repository.TrainRepository;
import repository.impl.DbTrainRepository;
import service.travel.TravelComposer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TravelComposerIntegrationTest {

    @Test
    void testRomaToBagnaraConnection() throws Exception {
        // Connessione reale al database PostgreSQL
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/trenical_db", "postgres", "FeLaFeL!Db!16!"
        );
        TrainRepository repository = new DbTrainRepository(connection);
        List<Train> allTrains = repository.getAllTrains();

        // Costruzione indice stazione → treni
        Map<String, List<Train>> index = new HashMap<>();
        for (Train train : allTrains) {
            Set<String> stations = new HashSet<>();
            stations.add(train.getDepartureStation());
            stations.add(train.getArrivalStation());
            for (TrainStop stop : train.getTrainStop()) {
                stations.add(stop.getStopStation());
            }
            for (String station : stations) {
                index.computeIfAbsent(station, k -> new ArrayList<>()).add(train);
            }
        }

        TravelComposer composer = new TravelComposer(index);

        // Query dell'utente: cerca da Roma a Bagnara dopo le 16:30 del 1/08
        InternalTrainQuery query = new InternalTrainQuery(
                "Roma Termini",
                "Bagnara",
                LocalDateTime.of(2025, 8, 1, 16, 30),
                true,
                1,
                false
        );

        List<TravelSolution> solutions = composer.findTravelByCriteria(query);

        if (solutions.isEmpty()) {
            fail("Nessuna soluzione trovata. Treni disponibili:\n" +
                    allTrains.stream()
                            .map(t -> String.format("- %s da %s (%s) a %s (%s)",
                                    t.getTrainID(),
                                    t.getDepartureStation(),
                                    t.getScheduledDeparture(),
                                    t.getArrivalStation(),
                                    t.getScheduledArrival()))
                            .reduce("", (a, b) -> a + b + "\n")
            );
        }

        // Stampa tutte le soluzioni trovate (opzionale, puoi rimuovere)
        for (TravelSolution solution : solutions) {
            System.out.println("➤ Soluzione trovata:");
            for (Train train : solution.getTrainList()) {
                System.out.printf("  - Treno %s da %s a %s (%s → %s)\n",
                        train.getTrainID(),
                        train.getDepartureStation(),
                        train.getArrivalStation(),
                        train.getScheduledDeparture(),
                        train.getScheduledArrival()
                );
            }
        }

        assertTrue(solutions.stream().anyMatch(s -> s.getTrainList().size() >= 1),
                "Ci si aspetta almeno una soluzione con uno o più treni");
    }}