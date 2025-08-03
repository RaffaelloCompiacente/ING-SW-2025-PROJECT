package service.travel;

import dto.InternalTrainQuery;
import dto.TravelSolution;
import model.Train;
import model.TrainStop;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;




public class TravelComposer{
    private final Map<String, List<Train>> trainByStation;
    private final int MIN_TRANSFER_MINUTES=10;
    private final int MAX_TRANSFER_MINUTES=30;
    private final int MAX_ALLOWED_CONNECTIONS=6;

    public TravelComposer(Map<String,List<Train>> indexStation){
        this.trainByStation=indexStation;
    }

    public List<TravelSolution> findTravelByCriteria(InternalTrainQuery query){
        List<List<Train>> validPaths = new ArrayList<>();
        Set<Train> usedTrains= new HashSet<>();
        List<Train> currentPath = new ArrayList<>();

        findPaths(
                query.getDepartureStation(),
                query.getDepartureTresholds(),
                query.getArrivalStation(),
                currentPath,
                usedTrains,
                validPaths,
                MAX_ALLOWED_CONNECTIONS //numero massimo di cambi
        );
        return validPaths.stream().map(TravelSolution::new).toList();
    }

    private void findPaths(
            String currentStation,
            LocalDateTime earliestDeparture,
            String destinationStation,
            List<Train> currentPath,
            Set<Train> usedTrains,
            List<List<Train>> allSolutions,
            int maxDepth
    ){
        if(currentPath.size()>maxDepth)return;
        if(!currentPath.isEmpty()){
            Train lastTrain=currentPath.get(currentPath.size()-1);
            if(reachesDestination(lastTrain,destinationStation)){
                allSolutions.add(new ArrayList<>(currentPath));
                return;
            }
        }

        for(Train candidate: trainByStation.getOrDefault(currentStation,List.of())){
            if(usedTrains.contains(candidate))continue;

            LocalDateTime candidateDeparture=getDepartureFrom(candidate,currentStation);
            if(candidateDeparture== null)continue;

            Duration gap= Duration.between(earliestDeparture,candidateDeparture);

            if(gap.toMinutes()<MIN_TRANSFER_MINUTES)continue;
            if(gap.toMinutes()>MAX_TRANSFER_MINUTES)continue;

            usedTrains.add(candidate);
            currentPath.add(candidate);

            String nextStation=getArrivalStationFrom(candidate,currentStation);
            LocalDateTime nextArrival=getArrivalAt(candidate,nextStation);

            findPaths(nextStation,nextArrival,destinationStation,currentPath,usedTrains,allSolutions,maxDepth);

            currentPath.remove(currentPath.size()-1);
            usedTrains.remove(candidate);
        }
    }

    private boolean reachesDestination(Train train,String station){
        if(train.getArrivalStation().equals(station))return true;
        return train.getTrainStop().stream().anyMatch(stop->stop.getStopStation().equals(station));
    }

    private LocalDateTime getDepartureFrom(Train train,String station){
        if(train.getDepartureStation().equals(station))return train.getScheduledDeparture();
        return train.getTrainStop().stream().filter(stop->stop.getStopStation().equals(station))
                .map(TrainStop::getStopDepartureDate).findFirst().orElse(null);
    }

    private LocalDateTime getArrivalAt(Train train,String station){
        if(train.getArrivalStation().equals(station))return train.getScheduledArrival();
        return train.getTrainStop().stream().filter(stop->stop.getStopStation().equals(station))
                .map(TrainStop::getStopArrivalDate).findFirst().orElse(null);
    }

    private String getArrivalStationFrom(Train train,String fromStation){
        List<TrainStop> stops=train.getTrainStop();
        for(int i=0;i<stops.size();i++){
            if(stops.get(i).getStopStation().equals(fromStation) && i+1<stops.size()){
                return stops.get(i+1).getStopStation();
            }
        }
        if(train.getDepartureStation().equals(fromStation)){
            return train.getArrivalStation();
        }
        return null;
    }



}