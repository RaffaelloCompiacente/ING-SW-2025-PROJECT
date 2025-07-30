package main.java.factory;

import main.java.model.*;
import main.java.dto.*;
import main.java.util.TrainClassPolicy;

import java.util.Map;
import java.util.EnumMap;
import main.java.util.*;

public class OtherTrainFactory implements TrainFactory{
    @Override
    public Train createTrain(TrainCreationRequest req){
        Map<TravelClass,Integer> input=req.getSeatsPerClass();
        TrainClassPolicy.validate(req.getTrainType(),input.keySet());
        Map<TravelClass,Integer> seats= new EnumMap<>(TravelClass.class);
        seats.putAll(input);
         return new Train(
                req.getTrainID(),
                req.getTrainType(),
                req.getDepartureTime(),
                req.getArrivalTime(),
                req.getDepartureStation(),
                req.getArrivalStation(),
                req.getTotalSeats(),
                seats,
                true
        );
    }
}