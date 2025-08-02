package service;


import adapter.InternalQueryMapper;
import com.trenical.grpc.train.TrainSearchRequest;
import com.trenical.grpc.train.TrainSearchResponse;
import com.trenical.grpc.train.TrainServiceGrpc;
import dto.InternalTrainQuery;
import dto.TravelSolution;
import io.grpc.stub.StreamObserver;
import java.util.List;


public class TrainServiceImpl extends TrainServiceGrpc.TrainServiceImplBase{
    @Override
    public void searchTrainByFilters(TrainSearchRequest req, StreamObserver<TrainSearchResponse> responseObserver){
        InternalTrainQuery itq= InternalQueryMapper.fromSearchTrainRequest(req);
        List<TravelSolution> validTravel = TravelComposer.findTravelByCriteria(itq);
        TrainSearchResponse response =InternalQueryMapper.toGrpcSearchTrainResponse(validTravel);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}