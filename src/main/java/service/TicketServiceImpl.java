/*package service;

import adapter.InternalQueryMapper;
import com.trenical.grpc.common.Common.TravelSolutionDTO;
import com.trenical.grpc.ticket.PreviewRequest;
import com.trenical.grpc.ticket.PreviewResponse;
import com.trenical.grpc.ticket.TicketServiceGrpc;
import com.trenical.grpc.train.TrainSearchRequest;
import com.trenical.grpc.train.TrainSearchResponse;
import com.trenical.grpc.train.TrainServiceGrpc;
import com.trenical.grpc.train.TrainServiceGrpc.TrainServiceBlockingStub;
import dto.InternalTrainQuery;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import model.Train;

import java.util.List;


public class TicketServiceImpl extends TicketServiceGrpc.TicketServiceImplBase{
    private final TrainServiceBlockingStub trainStub;

    public TicketServiceImpl(){
            ManagedChannel channel= ManagedChannelBuilder
                    .forAddress("localhost",50051)
                    .usePlaintext()
                    .build();
            this.trainStub= TrainServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public void getTicketPreviews(PreviewRequest request, StreamObserver<PreviewResponse> responseObserver) {
        InternalTrainQuery itq= InternalQueryMapper.fromPreviewRequest(request);
        TrainSearchRequest req=InternalQueryMapper.toGrpcSearchTrainRequest(itq);
        TrainSearchResponse response=trainStub.searchTrainByFilters(req);
        List<TravelSolutionDTO> resultsTravel = response.getTravelsList();
        List<dto.TravelSolution> mappedList=InternalQueryMapper.mapTravelList(resultsTravel);
        Map<Train, PriceResults> priceForPreviews= TicketPriceHandler.calculatePreviewPrice(mappedList);
        PreviewResponse previewResponse= PreviewCreator.generatePreviews(priceForPreviews);
        responseObserver.onNext(previewResponse);
        responseObserver.onCompleted();
    }
}*/