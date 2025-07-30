package main.java.factory;

import main.java.model.*;
import main.java.dto.*;
import main.java.util.TrainClassPolicy;

import java.util.Map;
import java.util.EnumMap;

public class RegionalTrainFactory implements TrainFactory{
    @Override
    public Train createTrain(TrainCreationRequest req){
        Map<TravelClass,Integer> input=req.getSeatsPerClass();
        TrainClassPolicy.validate(req.getTrainType(),input.keySet());
        Map<TravelClass,Integer> seats= new EnumMap<>(TravelClass.class);
        seats.put(TravelClass.SECONDA,input.getOrDefault(TravelClass.SECONDA,req.getTotalSeats()));
        return new Train(
                req.getTrainID(),
                req.getTrainType(),
                req.getDepartureTime(),
                req.getArrivalTime(),
                req.getDepartureStation(),
                req.getArrivalStation(),
                req.getTotalSeats(),
                seats,
                false
        );
    }
}