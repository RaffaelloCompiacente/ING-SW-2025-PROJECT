package service.travel;

import dto.InternalTrainQuery;
import dto.TravelSolution;
import model.Train;
import model.TrainStop;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TravelComposer (adattato a GTFS, modelli invariati):
 * - Usa i Train e TrainStop già mappati dal repository (orari LocalDateTime ancorati alla serviceDate).
 * - Rispetta vincoli temporali (min/max cambio).
 * - Evita loop di stazioni e riuso dello stesso treno.
 * - Consente di scendere a QUALSIASI stazione successiva del treno scelto.
 * - Limita il numero di leg per soluzione.
 *
 * Nota: la lista train.getTrainStop() include già TUTTE le fermate (inclusi capolinea).
 */
public class TravelComposer {

    private final Map<String, List<Train>> trainByStation;

    private final int MIN_TRANSFER_MINUTES = 10;
    private final int MAX_TRANSFER_MINUTES = 40;
    private final int MAX_TRAINS_PER_SOLUTION = 6;

    public TravelComposer(Map<String, List<Train>> indexStation) {
        this.trainByStation = (indexStation != null) ? indexStation : Map.of();
    }

    public List<TravelSolution> findTravelByCriteria(InternalTrainQuery query) {
        String origin = query.getDepartureStation();     // chiave come nel tuo index
        String destination = query.getArrivalStation();  // chiave come nel tuo index
        LocalDateTime earliestDeparture = query.getDepartureTresholds();

        // Mantengo la tua logica originale (se vorrai invertire, lo facciamo dopo):
        final int maxLegs = (query.getWithoutConnections() ? 1:MAX_TRAINS_PER_SOLUTION );

        List<List<Train>> paths = new ArrayList<>();
        dfs(origin, destination, earliestDeparture,
                new ArrayList<>(), new HashSet<>(), new HashSet<>(Set.of(origin)),
                paths, maxLegs);

        return paths.stream().map(TravelSolution::new).toList();
    }

    private void dfs(String currentStation,
                     String destination,
                     LocalDateTime earliestDeparture,
                     List<Train> currentPath,
                     Set<Train> usedTrains,
                     Set<String> visitedStations,
                     List<List<Train>> solutions,
                     int maxLegs) {

        if (currentPath.size() >= maxLegs) return;

        List<Train> candidates = trainByStation.getOrDefault(currentStation, List.of());
        if (candidates.isEmpty()) return;

        for (Train train : candidates) {
            if (usedTrains.contains(train)) continue;

            LocalDateTime departHere = departureAt(train, currentStation);
            if (departHere == null) continue;

            long waitMin = Duration.between(earliestDeparture, departHere).toMinutes();
            if (!currentPath.isEmpty()) {
                if (waitMin < MIN_TRANSFER_MINUTES) continue;
                if (waitMin > MAX_TRANSFER_MINUTES) continue;
            } else if (waitMin < 0) {
                // primo leg: non partire prima della soglia richiesta
                continue;
            }

            List<NextHop> downstream = downstreamStops(train, currentStation);
            if (downstream.isEmpty()) continue;

            usedTrains.add(train);
            currentPath.add(train);

            boolean reachesDirect = downstream.stream().anyMatch(h -> destination.equals(h.station));
            if (reachesDirect) {
                solutions.add(new ArrayList<>(currentPath));
                // backtrack: non esplorare discese intermedie peggiori del diretto
                currentPath.remove(currentPath.size() - 1);
                usedTrains.remove(train);
                continue;
            }

            if (currentPath.size() < maxLegs) {
                for (NextHop hop : downstream) {
                    String nextStation = hop.station;
                    if (visitedStations.contains(nextStation)) continue;

                    visitedStations.add(nextStation);
                    dfs(nextStation, destination, hop.arrival,
                            currentPath, usedTrains, visitedStations, solutions, maxLegs);
                    visitedStations.remove(nextStation);
                }
            }

            currentPath.remove(currentPath.size() - 1);
            usedTrains.remove(train);
        }
    }

    /**
     * Orario di partenza del 'train' dalla stazione 'station'.
     * Usa TrainStop (GTFS stop_times). Se è null (es. stazione finale), tenta un fallback
     * sullo scheduledDeparture se la stazione coincide con quella di partenza del treno.
     */
    private LocalDateTime departureAt(Train train, String station) {
        for (TrainStop s : train.getTrainStop()) {
            if (station.equals(s.getStopStation())) {
                LocalDateTime dep = s.getStopDepartureDate();
                if (dep != null) return dep;
                // Se è lo stop iniziale e la departure è null, fallback sul campo del treno
                if (station.equals(train.getDepartureStation())) {
                    return train.getScheduledDeparture();
                }
                return null;
            }
        }
        return null;
    }

    /**
     * Restituisce tutte le possibili "discese" successive alla stazione 'fromStation',
     * con l'orario di ARRIVO in quella stazione (per calcolare i vincoli di attesa).
     */
    private List<NextHop> downstreamStops(Train train, String fromStation) {
        List<NextHop> result = new ArrayList<>();
        List<Node> route = buildRoute(train);
        int idx = indexOfStation(route, fromStation);
        if (idx < 0) return result;

        for (int i = idx + 1; i < route.size(); i++) {
            Node n = route.get(i);
            if (n.arrival != null) result.add(new NextHop(n.station, n.arrival));
        }
        return result;
    }

    /**
     * Costruisce la rotta a partire da TUTTI gli stop GTFS (già completi).
     * NIENTE nodi fittizi: la lista train.getTrainStop() include già capolinea.
     */
    private List<Node> buildRoute(Train train) {
        List<Node> route = new ArrayList<>(Math.max(8, train.getTrainStop().size()));
        for (TrainStop s : train.getTrainStop()) {
            route.add(new Node(s.getStopStation(), s.getStopArrivalDate(), s.getStopDepartureDate()));
        }
        return route;
    }

    private int indexOfStation(List<Node> route, String station) {
        for (int i = 0; i < route.size(); i++) {
            if (route.get(i).station.equals(station)) return i;
        }
        return -1;
    }

    private static class Node {
        final String station;
        final LocalDateTime arrival;   // può essere null per il primo stop
        final LocalDateTime departure; // può essere null per l’ultimo stop
        Node(String station, LocalDateTime arrival, LocalDateTime departure) {
            this.station = station; this.arrival = arrival; this.departure = departure;
        }
    }

    private static class NextHop {
        final String station;
        final LocalDateTime arrival;
        NextHop(String station, LocalDateTime arrival) {
            this.station = station; this.arrival = arrival;
        }
    }

    /*//DEBUGGING
    // === DEBUG/VERIFY HOOKS ===
    public void verifyCompose(InternalTrainQuery query) {
        String origin = query.getDepartureStation();
        String destination = query.getArrivalStation();

        System.out.printf("%n[VERIFY] %s -> %s  earliest=%s  maxLegs=%d%n",
                origin, destination, query.getDepartureTresholds(),
                (query.getWithoutConnections() ? 1 : MAX_TRAINS_PER_SOLUTION));

        var sols = findTravelByCriteria(query);
        System.out.println("[VERIFY] solutions found: " + sols.size());

        if (!sols.isEmpty()) {
            dumpSolutions(sols);
            return;
        }

        System.out.println("[VERIFY] No solutions. Running step-by-step diagnosis...");
        diagnoseNoSolution(query);
    }

    private void dumpSolutions(List<TravelSolution> sols) {
        int i = 1;
        for (TravelSolution ts : sols) {
            System.out.println("  Solution #" + (i++) + " legs=" + ts.getTrainList().size());
            for (var t : ts.getTrainList()) {
                var dep = departureAt(t, t.getDepartureStation());
                var arr = arrivalAt(t, t.getArrivalStation());
                System.out.println("    - " + t.getTrainID() + "  "
                        + t.getDepartureStation() + " (" + dep + ") -> "
                        + t.getArrivalStation() + " (" + arr + ")");
            }
        }
    }

    private void diagnoseNoSolution(InternalTrainQuery query) {
        String origin = query.getDepartureStation();
        String destination = query.getArrivalStation();
        var earliest = query.getDepartureTresholds();

        List<Train> firstLegs = trainByStation.getOrDefault(origin, List.of());
        System.out.println("[D1] candidates at ORIGIN '" + origin + "': " + firstLegs.size());
        if (firstLegs.isEmpty()) {
            System.out.println("[D1] Nessun treno indicizzato per l'origine. Controlla normalizzazione nomi stazione.");
            return;
        }

        for (Train t1 : firstLegs) {
            LocalDateTime dep1 = departureAt(t1, origin);
            if (dep1 == null) {
                System.out.println("  - SKIP " + key(t1) + " (no departureAt@" + origin + ")");
                continue;
            }
            long wait0 = java.time.Duration.between(earliest, dep1).toMinutes();
            if (wait0 < 0) {
                System.out.println("  - SKIP " + key(t1) + " (parte prima di earliest: " + wait0 + " min)");
                continue;
            }
            var downs = downstreamStops(t1, origin);
            System.out.println("  - TRY  " + key(t1) + " depart@" + dep1 + " downstream=" + downs.size());

            // 2.a: controlla diretto
            boolean direct = downs.stream().anyMatch(h -> destination.equals(h.station));
            if (direct) {
                System.out.println("    * RAGGIUNGE DIRETTO " + destination + " (ma la tua ricerca chiedeva anche cambi?)");
            }

            // 2.b: per ogni possibile stazione di cambio, cerca un secondo treno compatibile
            for (NextHop hop : downs) {
                String change = hop.station;
                List<Train> secondLegs = trainByStation.getOrDefault(change, List.of());
                System.out.println("    > Change @" + change + " arr=" + hop.arrival + "  candidates=" + secondLegs.size());
                if (secondLegs.isEmpty()) {
                    System.out.println("      - Nessun treno indicizzato su " + change + " (indice? nomi stazione?)");
                    continue;
                }
                boolean foundAny = false;
                for (Train t2 : secondLegs) {
                    if (t2.equals(t1)) continue; // non riusare stesso treno
                    LocalDateTime dep2 = departureAt(t2, change);
                    if (dep2 == null) {
                        System.out.println("      - SKIP " + key(t2) + " (no departureAt@" + change + ")");
                        continue;
                    }
                    long wait = java.time.Duration.between(hop.arrival, dep2).toMinutes();
                    if (wait < MIN_TRANSFER_MINUTES) {
                        System.out.println("      - SKIP " + key(t2) + " wait=" + wait + " < MIN(" + MIN_TRANSFER_MINUTES + ")");
                        continue;
                    }
                    if (wait > MAX_TRANSFER_MINUTES) {
                        System.out.println("      - SKIP " + key(t2) + " wait=" + wait + " > MAX(" + MAX_TRANSFER_MINUTES + ")");
                        continue;
                    }
                    // controlla se il secondo treno arriva a destinazione (diretto o proseguibile)
                    boolean t2Reaches = reaches(trainRoute(t2), destination, change);
                    System.out.println("      + OK   " + key(t2) + " wait=" + wait + "min  reachesDest=" + t2Reaches);
                    if (t2Reaches) foundAny = true;
                }
                if (!foundAny) {
                    System.out.println("      (Nessun secondo leg valido da " + change + " entro la finestra " +
                            MIN_TRANSFER_MINUTES + "-" + MAX_TRANSFER_MINUTES + " min)");
                }
            }
        }
    }

    private boolean reaches(List<Node> route, String target, String afterStation) {
        int i = indexOfStation(route, afterStation);
        if (i < 0) return false;
        for (int j = i + 1; j < route.size(); j++) {
            if (target.equals(route.get(j).station)) return true;
        }
        return false;
    }

    private String key(Train t) {
        return t.getTrainID() + " [" + t.getDepartureStation() + "→" + t.getArrivalStation() + "]";
    }

    private List<Node> trainRoute(Train t) { // alias leggibile di buildRoute
        return buildRoute(t);
    }

    private LocalDateTime arrivalAt(Train train, String station) {
        for (TrainStop s : train.getTrainStop()) {
            if (station.equals(s.getStopStation())) {
                return s.getStopArrivalDate();
            }
        }
        return null;
    }*/



}
