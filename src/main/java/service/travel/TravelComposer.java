package service.travel;

import dto.InternalTrainQuery;
import dto.TravelSolution;
import model.Train;
import model.TrainStop;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TravelComposer riscritto:
 * - Rispetta vincoli temporali (min/max cambio).
 * - Evita loop per stazioni e riuso dello stesso treno.
 * - Consente di scendere a QUALSIASI stazione successiva del treno scelto.
 * - Limita il numero di leg (treni) per soluzione.
 */
public class TravelComposer {

    private final Map<String, List<Train>> trainByStation;

    private final int MIN_TRANSFER_MINUTES = 10;
    private final int MAX_TRANSFER_MINUTES = 12 * 60;
    private final int MAX_TRAINS_PER_SOLUTION = 6;

    public TravelComposer(Map<String, List<Train>> indexStation) {
        this.trainByStation = indexStation != null ? indexStation : Map.of();
    }

    public List<TravelSolution> findTravelByCriteria(InternalTrainQuery query) {
        String origin = query.getDepartureStation();
        String destination = query.getArrivalStation();
        LocalDateTime earliestDeparture = query.getDepartureTresholds();

        //  Limite dinamico in base al flag connections
        final int maxLegs = (query.getWithoutConnections() ? MAX_TRAINS_PER_SOLUTION : 1);

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

        // Se ho già raggiunto il numero max di treni, non posso proseguire
        if (currentPath.size() >= maxLegs) return;

        // ... dentro dfs(...)

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
                continue;
            }

            List<NextHop> downstream = downstreamStops(train, currentStation);
            if (downstream.isEmpty()) continue;

            usedTrains.add(train);
            currentPath.add(train);

            // 👇 NOVITÀ: se questo treno arriva alla destinazione, proponi SOLO il diretto
            boolean reachesDirect = downstream.stream().anyMatch(h -> destination.equals(h.station));
            if (reachesDirect) {
                solutions.add(new ArrayList<>(currentPath));
                // backtrack: non esplorare discese intermedie che porterebbero a soluzioni peggiori
                currentPath.remove(currentPath.size() - 1);
                usedTrains.remove(train);
                continue;
            }

            // Altrimenti, se sono consentiti i cambi, esplora le discese intermedie
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

            // backtrack
            currentPath.remove(currentPath.size() - 1);
            usedTrains.remove(train);
        }
    }

    private LocalDateTime departureAt(Train train, String station) {
        if (station.equals(train.getDepartureStation())) return train.getScheduledDeparture();
        for (TrainStop s : train.getTrainStop()) {
            if (station.equals(s.getStopStation())) return s.getStopDepartureDate();
        }
        return null;
    }

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

    private List<Node> buildRoute(Train train) {
        List<Node> route = new ArrayList<>();
        route.add(new Node(train.getDepartureStation(), null, train.getScheduledDeparture()));
        for (TrainStop s : train.getTrainStop()) {
            route.add(new Node(s.getStopStation(), s.getStopArrivalDate(), s.getStopDepartureDate()));
        }
        route.add(new Node(train.getArrivalStation(), train.getScheduledArrival(), null));
        return route;
    }

    private int indexOfStation(List<Node> route, String station) {
        for (int i = 0; i < route.size(); i++) if (route.get(i).station.equals(station)) return i;
        return -1;
    }

    private static class Node {
        final String station;
        final LocalDateTime arrival;   // null per la prima
        final LocalDateTime departure; // null per l’ultima
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
}