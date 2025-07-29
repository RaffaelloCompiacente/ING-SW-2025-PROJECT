package factory;

import model.Train;
import dto.TrainCreationRequest;

public interface TrainFactory{
    Train createTrain(TrainCreationRequest request);
}