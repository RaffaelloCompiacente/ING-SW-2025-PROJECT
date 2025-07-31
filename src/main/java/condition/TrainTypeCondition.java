package condition;

import model.TrainType;
import model.Train;

import java.time.LocalDate;
import java.util.List;


public class TrainTypeCondition{
    private final List<TrainType> validTrainTypes;

    public TrainTypeCondition(List<TrainType> trainTypes){
        this.validTrainTypes=trainTypes;
    }

    boolean isSatisfiedBy(Customer customer, Train train, LocalDate date){
        return validTrainTypes.contains(train.getTrainType());
    }
}