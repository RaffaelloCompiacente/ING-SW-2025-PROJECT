package repository;

import model.Train;
import java.util.List;
import java.util.Optional;

public interface TrainRepository{
    List<Train> getAllTrains();
    Optional<Train> findById(String trainId);
    void save(Train train);
    void update(Train train);
    void delete(String trainId);
}