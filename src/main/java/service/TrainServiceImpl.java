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
        if(trainIndexCache==null){
            List<Train> allTrains=trainRepository.getAllTrains();
            trainIndexCache=buildTrainIndex(allTrains);
        }
        TravelComposer travelComposer= new TravelComposer(trainIndexCache);
        List<TravelSolution> validTravel = travelComposer.findTravelByCriteria(itq);
        TrainSearchResponse response =InternalQueryMapper.mapToGrpcSearchTrainResponse(validTravel);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Map<String, List<Train>> buildTrainIndex(List<Train> trains){
        Map<String, List<Train>> map=new HashMap<>();
        for(Train train:trains){
            Set<String> stations= new HashSet<>();
            stations.add(train.getDepartureStation());
            stations.add(train.getArrivalStation());
            for(TrainStop stop: train.getTrainStop()){
                stations.add(stop.getStopStation());
            }
            for(String station:stations){
                map.computeIfAbsent(station,k-> new ArrayList<>()).add(train);
            }
        }
        return map;
    }
}