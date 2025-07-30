package main.java.factory;

import main.java.model.Train;
import main.java.dto.TrainCreationRequest;

public interface TrainFactory{
    Train createTrain(TrainCreationRequest request);
}