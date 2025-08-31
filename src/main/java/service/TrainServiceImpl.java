package service;


import adapter.InternalQueryMapper;
import com.trenical.grpc.train.TrainSearchRequest;
import com.trenical.grpc.train.TrainSearchResponse;
import com.trenical.grpc.train.TrainServiceGrpc;
import dto.InternalTrainQuery;
import dto.TravelSolution;
import factory.TrainRepositoryFactory;
import io.grpc.stub.StreamObserver;
import model.Train;
import model.TrainStop;
import repository.TrainRepository;
import service.travel.TravelComposer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class TrainServiceImpl extends TrainServiceGrpc.TrainServiceImplBase{
    private final TrainRepository trainRepository;
    private Map<String,List<Train>> trainIndexCache;

    public TrainServiceImpl() throws IOException{
        this.trainRepository = TrainRepositoryFactory.createTrainRepository();
        this.trainIndexCache= null;
    }
    @Override
    public void searchTrainByFilters(TrainSearchRequest req, StreamObserver<TrainSearchResponse> responseObserver){
        InternalTrainQuery itq= InternalQueryMapper.fromSearchTrainRequest(req);
        if(trainIndexCache==null) {
           // List<Train> allTrains = trainRepository.getAllTrains();
            trainIndexCache = buildTrainIndex(itq);
        }
        TravelComposer travelComposer= new TravelComposer(trainIndexCache);
        List<TravelSolution> validTravel = travelComposer.findTravelByCriteria(itq);
        TrainSearchResponse response =InternalQueryMapper.mapToGrpcSearchTrainResponse(validTravel);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    // indice per la data richiesta, includendo gli overnight di day-1
    private Map<String, List<Train>> buildTrainIndex(InternalTrainQuery query) {
        LocalDate day = query.getDepartureTresholds().toLocalDate();

        // 1) trip attivi nel giorno richiesto
        List<Train> trains = new ArrayList<>(trainRepository.getAllTrains(day));

        // 2) (consigliato) aggiungi i trip partiti il giorno prima che toccano il giorno 'day'
        List<Train> prev = trainRepository.getAllTrains(day.minusDays(1));
        LocalDateTime start = day.atStartOfDay();
        LocalDateTime end   = day.plusDays(1).atStartOfDay();

        for (Train t : prev) {
            boolean touchesDay = t.getTrainStop().stream().anyMatch(s -> {
                LocalDateTime a = s.getStopArrivalDate();
                LocalDateTime d = s.getStopDepartureDate();
                return (a != null && !a.isBefore(start) && a.isBefore(end))
                        || (d != null && !d.isBefore(start) && d.isBefore(end));
            });
            if (touchesDay) trains.add(t);
        }

        // 3) costruzione indice stazione → treni (come già facevi)
        Map<String, List<Train>> map = new HashMap<>(Math.max(16, trains.size()*2));
        for (Train train : trains) {
            Set<String> stations = new HashSet<>();
            stations.add(train.getDepartureStation());
            stations.add(train.getArrivalStation());
            for (TrainStop stop : train.getTrainStop()) {
                stations.add(stop.getStopStation());
            }
            for (String station : stations) {
                map.computeIfAbsent(station, k -> new ArrayList<>()).add(train);
            }
        }
        return map;
    }

}