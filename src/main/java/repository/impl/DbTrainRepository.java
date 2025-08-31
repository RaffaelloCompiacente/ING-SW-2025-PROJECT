package repository.impl;

import model.Train;
import model.TrainStop;
import model.TrainStatus;
import model.TrainType;
import model.TravelClass;
import repository.TrainRepository;

import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;

public class DbTrainRepository implements TrainRepository {

    private final Connection connection;

    public DbTrainRepository(Connection connection) {
        this.connection = connection;
    }

    /* =========================
       API pubblica (read-only)
       ========================= */

    // repository/impl/DbTrainRepository.java (estratto rilevante)
    @Override
    public List<Train> getAllTrains(LocalDate serviceDate) {
        List<Train> trains = new ArrayList<>();
        final String sql = "SELECT trip_id FROM trip";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tripId = rs.getString("trip_id");
                loadTrainFromTrip(tripId, serviceDate).ifPresent(trains::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore in getAllTrains(" + serviceDate + ")", e);
        }
        return trains;
    }

    @Override
    public Optional<Train> findById(String trainId, LocalDate serviceDate) {
        return loadTrainFromTrip(trainId, serviceDate);
    }

    /* ============================================================
       Helpers principali: mapping da trip/stop_times & co.
       ============================================================ */

    private Optional<Train> loadTrainFromTrip(String tripId, LocalDate serviceDate) {
        final String sql = """

            WITH bounds AS (
              SELECT trip_id, MIN(stop_sequence) AS min_seq, MAX(stop_sequence) AS max_seq
              FROM stop_times
              WHERE trip_id = ?
              GROUP BY trip_id
            )
            SELECT
              t.trip_id,
              t.status                         AS trip_status,
              t.service_id,

              tr.train_id,
              tr.train_type_code,
              tr.rolling_stock_id,
              tr.reservable                    AS train_reservable,

              tt.display_name                  AS train_type_name,

              fs.stop_id                       AS dep_stop_id,
              fs.departure_time                AS dep_time,   -- TIME
              ls.stop_id                       AS arr_stop_id,
              ls.arrival_time                  AS arr_time    -- TIME
            FROM trip t
            JOIN bounds b           ON b.trip_id = t.trip_id
            JOIN train tr           ON tr.train_id = t.train_id
            LEFT JOIN train_type tt ON tt.train_type_code = tr.train_type_code
            JOIN stop_times fs      ON fs.trip_id = t.trip_id AND fs.stop_sequence = b.min_seq
            JOIN stop_times ls      ON ls.trip_id = t.trip_id AND ls.stop_sequence = b.max_seq
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                String serviceId = rs.getString("service_id");
                if (!isServiceActiveOn(serviceId, serviceDate)) return Optional.empty();

                int depStopId = rs.getInt("dep_stop_id");
                int arrStopId = rs.getInt("arr_stop_id");
                Time depTime  = rs.getTime("dep_time");
                Time arrTime  = rs.getTime("arr_time");

                LocalDateTime scheduledDeparture = depTime != null
                        ? serviceDate.atTime(depTime.toLocalTime())
                        : serviceDate.atStartOfDay();
                LocalDateTime scheduledArrival   = arrTime != null
                        ? serviceDate.atTime(arrTime.toLocalTime())
                        : scheduledDeparture;
                if (scheduledArrival.isBefore(scheduledDeparture)) {
                    scheduledArrival = scheduledArrival.plusDays(1); // rollover oltre mezzanotte
                }

                String departureName = getStationNameByStopId(depStopId);
                String arrivalName   = getStationNameByStopId(arrStopId);

                String dbTypeName = rs.getString("train_type_name"); // es. "Frecciarossa"
                String dbTypeCode = rs.getString("train_type_code"); // es. "FR"
                TrainType type    = parseTrainType(dbTypeName, dbTypeCode);

                TrainStatus status = parseTrainStatus(rs.getString("trip_status"));
                boolean reservable = rs.getBoolean("train_reservable");

                String rollingStockId = rs.getString("rolling_stock_id");
                Map<TravelClass, Integer> seatsPerClass = getSeatsPerClassFromRollingStock(rollingStockId);
                int totalSeats = seatsPerClass.values().stream().mapToInt(Integer::intValue).sum();

                List<TrainStop> stops = getTrainStops(tripId, scheduledDeparture);

                Train train = new Train(
                        tripId,
                        type,
                        scheduledDeparture,
                        scheduledArrival,
                        departureName,
                        arrivalName,
                        totalSeats,
                        seatsPerClass,
                        reservable,
                        stops
                );
                trySetTrainStatus(train, status);

                return Optional.of(train);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel mapping trip->Train per trip_id=" + tripId, e);
        }
    }

    /** Posti per classe dalla composizione rotabili (rolling_stock_layout). */
    private Map<TravelClass, Integer> getSeatsPerClassFromRollingStock(String rollingStockId) throws SQLException {
        Map<TravelClass, Integer> map = new EnumMap<>(TravelClass.class);
        if (rollingStockId == null || rollingStockId.isEmpty()) return map;

        final String sql = """
            SELECT class_code, seats
            FROM rolling_stock_layout
            WHERE rolling_stock_id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, rollingStockId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String code = rs.getString("class_code");
                    int seats   = rs.getInt("seats");
                    TravelClass cls = parseTravelClass(code);
                    map.merge(cls, seats, Integer::sum);
                }
            }
        }
        return map;
    }

    /** Fermate ordinate di un trip con gestione rollover rispetto alla data base. */
    private List<TrainStop> getTrainStops(String tripId, LocalDateTime scheduledDeparture) throws SQLException {
        final String sql = """
            SELECT
              st.stop_id,
              st.arrival_time,     -- TIME, può essere NULL
              st.departure_time,   -- TIME, può essere NULL
              st.stop_sequence
            FROM stop_times st
            WHERE st.trip_id = ?
            ORDER BY st.stop_sequence
            """;

        List<TrainStop> list = new ArrayList<>();
        LocalDate baseDate = scheduledDeparture.toLocalDate();
        LocalTime lastSeen = null;
        int dayOffset = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int stopId = rs.getInt("stop_id");
                    String station = getStationNameByStopId(stopId);

                    Time arrT = rs.getTime("arrival_time");
                    Time depT = rs.getTime("departure_time");

                    LocalDateTime arr = null, dep = null;
                    if (arrT != null) {
                        LocalTime t = arrT.toLocalTime();
                        if (lastSeen != null && t.isBefore(lastSeen)) dayOffset++;
                        arr = baseDate.plusDays(dayOffset).atTime(t);
                        lastSeen = t;
                    }
                    if (depT != null) {
                        LocalTime t = depT.toLocalTime();
                        if (lastSeen != null && t.isBefore(lastSeen)) dayOffset++;
                        dep = baseDate.plusDays(dayOffset).atTime(t);
                        lastSeen = t;
                    }

                    list.add(new TrainStop(station, arr, dep));
                }
            }
        }
        return list;
    }

    private String getStationNameByStopId(int stopId) throws SQLException {
        final String sql = "SELECT name FROM stazioni_treni_italiane WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stopId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("name");
            }
        }
        return "Sconosciuta";
    }

    /* =========================
       Calendario di servizio
       ========================= */

    private boolean isServiceActiveOn(String serviceId, LocalDate date) throws SQLException {
        if (serviceId == null) return false;

        if (existsInAdded(serviceId, date))   return true;
        if (existsInRemoved(serviceId, date)) return false;

        final String sql = """
            SELECT start_date, end_date,
                   mon, tue, wed, thu, fri, sat, sun
            FROM service
            WHERE service_id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, serviceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;

                LocalDate start = rs.getDate("start_date").toLocalDate();
                LocalDate end   = rs.getDate("end_date").toLocalDate();
                if (date.isBefore(start) || date.isAfter(end)) return false;

                boolean[] days = new boolean[] {
                        rs.getBoolean("mon"),
                        rs.getBoolean("tue"),
                        rs.getBoolean("wed"),
                        rs.getBoolean("thu"),
                        rs.getBoolean("fri"),
                        rs.getBoolean("sat"),
                        rs.getBoolean("sun")
                };
                int idx = switch (date.getDayOfWeek()) {
                    case MONDAY -> 0; case TUESDAY -> 1; case WEDNESDAY -> 2;
                    case THURSDAY -> 3; case FRIDAY -> 4; case SATURDAY -> 5; case SUNDAY -> 6;
                };
                return days[idx];
            }
        }
    }

    private boolean existsInAdded(String serviceId, LocalDate date) throws SQLException {
        final String sql = "SELECT 1 FROM service WHERE service_id = ? AND ? = ANY(added_dates)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, serviceId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean existsInRemoved(String serviceId, LocalDate date) throws SQLException {
        final String sql = "SELECT 1 FROM service WHERE service_id = ? AND ? = ANY(removed_dates)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, serviceId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /* =========================
       Parser/mapping robusti
       ========================= */

    private TrainStatus parseTrainStatus(String dbStatus) {
        if (dbStatus == null) return TrainStatus.ON_TIME;
        try {
            return TrainStatus.valueOf(dbStatus.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return TrainStatus.ON_TIME;
        }
    }

    private TrainType parseTrainType(String displayName, String code) {
        if (displayName != null) {
            String norm = displayName.trim().toUpperCase(Locale.ROOT)
                    .replace(' ', '_')
                    .replace('-', '_');
            try { return TrainType.valueOf(norm); } catch (IllegalArgumentException ignored) { }
        }
        if (code != null) {
            String c = code.trim().toUpperCase(Locale.ROOT);
            switch (c) {
                case "FR", "FRECCIAROSSA"      -> { return tryTrainType("FRECCIAROSSA", "HIGH_SPEED", "ALTA_VELOCITA"); }
                case "FA", "FRECCIARGENTO"     -> { return tryTrainType("FRECCIARGENTO"); }
                case "IC", "INTERCITY"         -> { return tryTrainType("INTERCITY"); }
                case "RV", "REGIONALE_VELOCE"  -> { return tryTrainType("REGIONALE_VELOCE"); }
                case "REG", "REGIONALE"        -> { return tryTrainType("REGIONALE", "REGIONAL"); }
            }
        }
        return tryTrainType("REGIONALE");
    }

    private TrainType tryTrainType(String... candidates) {
        for (String c : candidates) {
            try { return TrainType.valueOf(c); } catch (Exception ignored) {}
        }
        return TrainType.values()[0];
    }

    private TravelClass parseTravelClass(String code) {
        if (code == null) return defaultClass();
        String c = code.trim().toUpperCase(Locale.ROOT);
        try { return TravelClass.valueOf(c); } catch (Exception ignored) {}
        switch (c) {
            case "1", "1A", "FIRST", "BUSINESS"   -> { return tryTravelClass("FIRST", "BUSINESS"); }
            case "2", "2A", "SECOND", "STANDARD"  -> { return tryTravelClass("SECOND", "STANDARD"); }
            case "PREMIUM"                        -> { return tryTravelClass("PREMIUM", "FIRST"); }
            default                               -> { return defaultClass(); }
        }
    }

    private TravelClass tryTravelClass(String... candidates) {
        for (String c : candidates) {
            try { return TravelClass.valueOf(c); } catch (Exception ignored) {}
        }
        return defaultClass();
    }

    private TravelClass defaultClass() {
        try { return TravelClass.valueOf("SECONDA"); } catch (Exception e) { return TravelClass.values()[0]; }
    }

    /* =========================
       Utility: set status via reflection
       ========================= */

    private void trySetTrainStatus(Train train, TrainStatus status) {
        if (status == null) return;
        try {
            Field f = Train.class.getDeclaredField("trainStatus");
            f.setAccessible(true);
            f.set(train, status);
        } catch (NoSuchFieldException | IllegalAccessException ignored) { }
    }
}

