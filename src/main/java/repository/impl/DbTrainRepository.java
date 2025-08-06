package repository.impl;

import model.Train;
import model.TrainStop;
import model.TrainType;
import model.TrainStatus;
import model.TravelClass;
import repository.TrainRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DbTrainRepository implements TrainRepository {

    private final Connection connection;

    public DbTrainRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM train")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                trains.add(mapTrain(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trains;
    }

    @Override
    public Optional<Train> findById(String trainId) {
        String query = "SELECT * FROM train WHERE train_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trainId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapTrain(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la ricerca del treno", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Train train) {
        // Implementazione INSERT (da scrivere nei prossimi passaggi)
    }

    @Override
    public void update(Train train) {
        delete(train.getTrainID());
        save(train);
    }

    @Override
    public void delete(String trainId) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM train WHERE train_id = ?")) {
            stmt.setString(1, trainId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'eliminazione del treno", e);
        }
    }

    private Train mapTrain(ResultSet rs) throws SQLException {
        String id = rs.getString("train_id");
        String departure = getStationName(rs.getInt("departure_station"));
        String arrival = getStationName(rs.getInt("arrival_station"));
        int totalSeats = rs.getInt("total_seats");
        LocalDateTime departureTime = rs.getTimestamp("scheduled_departure").toLocalDateTime();
        LocalDateTime arrivalTime = rs.getTimestamp("scheduled_arrival").toLocalDateTime();
        TrainType type = TrainType.valueOf(rs.getString("train_type"));
        TrainStatus status = TrainStatus.valueOf(rs.getString("train_status"));
        boolean reservable = rs.getBoolean("reservable");

        Map<TravelClass, Integer> seatsPerClass = getSeatsPerClass(id, departureTime);
        List<TrainStop> stops = getTrainStops(id, departureTime);

        return new Train(id, type, departureTime, arrivalTime, departure, arrival, totalSeats, seatsPerClass, reservable, stops);
    }

    private Map<TravelClass, Integer> getSeatsPerClass(String trainId, LocalDateTime scheduledDeparture) throws SQLException {
        String query = "SELECT * FROM train_seat_class WHERE train_id = ? AND scheduled_departure = ?";
        Map<TravelClass, Integer> map = new HashMap<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trainId);
            stmt.setTimestamp(2, Timestamp.valueOf(scheduledDeparture));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TravelClass cls = TravelClass.valueOf(rs.getString("travel_class"));
                int seats = rs.getInt("seats");
                map.put(cls, seats);
            }
        }
        return map;
    }

    private List<TrainStop> getTrainStops(String trainId, LocalDateTime scheduledDeparture) throws SQLException {
        String query = "SELECT * FROM train_stop WHERE train_id = ? AND scheduled_departure = ? ORDER BY stop_order";
        List<TrainStop> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trainId);
            stmt.setTimestamp(2, Timestamp.valueOf(scheduledDeparture));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String station = getStationName(rs.getInt("stop_station"));
                LocalDateTime arrival = rs.getTimestamp("stop_arrival_date") != null ? rs.getTimestamp("stop_arrival_date").toLocalDateTime() : null;
                LocalDateTime departure = rs.getTimestamp("stop_departure_date") != null ? rs.getTimestamp("stop_departure_date").toLocalDateTime() : null;
                list.add(new TrainStop(station, arrival, departure));
            }
        }
        return list;
    }

    private String getStationName(int stationId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT name FROM stazioni_treni_italiane WHERE id = ?")) {
            stmt.setInt(1, stationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("name");
        }
        return "Sconosciuta";
    }
}