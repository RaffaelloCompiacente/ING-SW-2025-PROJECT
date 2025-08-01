package dto;

import model.TrainType;
import model.TravelClass;
import model.TrainStop;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;


public class TrainCreationRequest{
    @NotNull
    @Size(min=3,max=4)
    private final String trainId;
    @NotNull
    @Size(min=2,max=22)
    @Pattern(regexp="[a-zA-Z]+")
    private final String departureStation;
    @NotNull
    @Size(min=2,max=22)
    @Pattern(regexp="[a-zA-Z]+")
    private final String arrivalStation;
    @NotNull
    @FutureOrPresent
    private final LocalDateTime departureTime;
    @NotNull
    @Future
    private final LocalDateTime arrivalTime;
    @NotNull
    private final TrainType trainType;
    @Min(100)
    @Max(1000)
    private final int totalSeats;
    //@Valid
    @Size(min=1,max=4)
    private final Map<@NotNull TravelClass,@NotNull @Min(1) Integer> seatsPerClass;
    @NotNull
    @Size(min=2)
    private final List<TrainStop> trainStops;

    public TrainCreationRequest(String ID,String dStation,String aStation,LocalDateTime dTime,LocalDateTime aTime,TrainType type,int seats,Map<TravelClass,Integer> seatsClass,List<TrainStop> stops){
        this.trainId=ID;
        this.departureStation=dStation;
        this.arrivalStation=aStation;
        this.departureTime=dTime;
        this.arrivalTime=aTime;
        this.trainType=type;
        this.totalSeats=seats;
        this.seatsPerClass=seatsClass;
        this.trainStops=stops;
    }

    public String getTrainID(){return this.trainId;}
    public TrainType getTrainType(){return this.trainType;}
    public LocalDateTime getDepartureTime(){return this.departureTime;}
    public LocalDateTime getArrivalTime(){return this.arrivalTime;}
    public String getDepartureStation(){return this.departureStation;}
    public String getArrivalStation(){return this.arrivalStation;}
    public int getTotalSeats(){return this.totalSeats;}
    public Map<TravelClass,Integer> getSeatsPerClass(){return this.seatsPerClass;}
    public List<TrainStop> getTrainStops(){return this.trainStops;}





}