package factory;

import model.*;
import dto.*;
import util.TrainClassPolicy;

import java.util.Map;
import java.util.EnumMap;
import util.*;

public class OtherTrainFactory implements TrainFactory{
    @Override
    public Train createTrain(TrainCreationRequest req){
        Map<TravelClass,Integer> input=req.getSeatsPerClass();
        TrainClassPolicy.validate(req.getTrainType(),input.keySet());
        Map<TravelClass,Integer> seats= new EnumMap<>(TravelClass.class);
        for(Map.Entry<TravelClass,Integer> entry:input.entrySet()){
           seats.put(entry.getKey(),entry.getValue());
        }
         return new Train(
                req.getTrainID(),
                req.getTrainType(),
                req.getDepartureTime(),
                req.getArrivalTime(),
                req.getDepartureStation(),
                req.getArrivalStation(),
                req.getTotalSeats(),
                seats
        );
    }
}