package adapter;


import com.trenical.grpc.common.Common;
import com.trenical.grpc.ticket.PreviewRequest;
import com.trenical.grpc.train.TrainSearchRequest;
import dto.InternalTrainQuery;
import dto.TravelSolution;
import model.TrainStop;
import model.TrainType;
import model.TravelClass;
import model.Train;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class InternalQueryMapper{
    //Lavoriamo col mapping perchè la logica interna di funzionamento del sistema non dovrebbe dipendere dalle richieste gRPC che sono instabili
    public static InternalTrainQuery fromPreviewRequest(PreviewRequest previewRequest){
            LocalDate date=previewRequest.hasDate()?LocalDate.parse(previewRequest.getDate()):LocalDate.now();
            LocalTime time= previewRequest.hasTime()?LocalTime.parse(previewRequest.getTime()):LocalTime.now();
            LocalDateTime dateAndTime=LocalDateTime.of(date,time);
            return new InternalTrainQuery(
                    previewRequest.getDepartureStation(),
                    previewRequest.getArrivalStation(),
                    dateAndTime,
                    previewRequest.hasAllowConnection()? previewRequest.getAllowConnection():false,
                    previewRequest.hasPassengerNumber()? previewRequest.getPassengerNumber():1,
                    previewRequest.hasReturnTicket()? previewRequest.getReturnTicket():false

            );
    }

    public static InternalTrainQuery fromSearchTrainRequest(TrainSearchRequest searchRequest){
        LocalDate date=LocalDate.parse(searchRequest.getDate());
        LocalTime time=LocalTime.parse(searchRequest.getTime());
        LocalDateTime dateAndTime=LocalDateTime.of(date,time);
        return new InternalTrainQuery(
                searchRequest.getDepartureStation(),
                searchRequest.getArrivalStation(),
                dateAndTime,
                searchRequest.getAllowConnection(),
                searchRequest.getPassengerNumber(),
                searchRequest.getReturnTicket()
        );
    }

    public static TrainSearchRequest toGrpcSearchTrainRequest(InternalTrainQuery itq){
        LocalDate date=itq.getDepartureTresholds().toLocalDate();
        LocalTime time=itq.getDepartureTresholds().toLocalTime();

        return TrainSearchRequest.newBuilder()
                .setDepartureStation(itq.getDepartureStation())
                .setArrivalStation(itq.getArrivalStation())
                .setDate(date.toString())
                .setTime(time.toString())
                .setAllowConnection(itq.getWithoutConnections())
                .setPassengerNumber(itq.getPassengerCount())
                .setReturnTicket(itq.getReturnTicket())
                .build();

    }

    public static model.TrainStop mapTrainStop(Common.TrainStopDTO trainStop){
        LocalDateTime arrivalTime=LocalDateTime.parse(trainStop.getArrivalTime());
        LocalDateTime departureTime=LocalDateTime.parse(trainStop.getDepartureTime());
        return new model.TrainStop(
                trainStop.getStation(),
                arrivalTime,
                departureTime
        );
    }

    public static Train mapTrain(Common.TrainDTO train){
        LocalDateTime departureTime= LocalDateTime.parse(train.getDepartureDate());
        LocalDateTime arrivalTime=LocalDateTime.parse(train.getArrivalDate());
        Map<String,Integer> requestMap=new HashMap<>(train.getSeatsPerClassMap());
        Map<TravelClass,Integer> seatsPerClass= new HashMap<>();
        for(Map.Entry<String,Integer> entry: requestMap.entrySet()){
            seatsPerClass.put(TravelClass.valueOf(entry.getKey()), entry.getValue());
        }
        List<TrainStop> trainStops=train.getStopsList().stream().map(InternalQueryMapper::mapTrainStop).toList();
        return new Train(
                train.getTrainId(),
                TrainType.valueOf(train.getType()),
                departureTime,
                arrivalTime,
                train.getOrigin(),
                train.getDestination(),
                train.getSeats(),
                seatsPerClass,
                train.getReservable(),
                trainStops
        );
    }

    public static TravelSolution mapTravelSolution(Common.TravelSolutionDTO travel){
        List<Train> treni=travel.getSolutionsList().stream().map(InternalQueryMapper::mapTrain).toList();
        return new TravelSolution(treni);
    };

    public static List<TravelSolution> mapTravelList(List<Common.TravelSolutionDTO> list){
        List<TravelSolution> travelSolutions=list.stream().map(InternalQueryMapper::mapTravelSolution).toList();
        return travelSolutions;
    };

}