package repository;

import model.Train;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainRepository{
    List<Train> getAllTrains(LocalDate serviceDate);
    Optional<Train> findById(String trainId,LocalDate serviceDate);
}