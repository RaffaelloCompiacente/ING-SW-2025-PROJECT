package model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

public class Train{
    private final String trainID;
    private final String departureStation;
    private final String arrivalStation;
    private final int totalSeats;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private Map<TravelClass,Integer> seatsPerClass;
    private final TrainType trainType;
    private TrainStatus trainStatus;
    private final boolean reservable;
    private final List<TrainStop> trainStops;

    public Train(String ID,TrainType type,LocalDateTime departureTime,LocalDateTime arrivalTime,String departure,String arrival, int trainSeats ,Map<TravelClass,Integer> classSeats, boolean reservation,List<TrainStop> stops){
        this.trainID=ID;
        this.trainType=type;
        this.scheduledDeparture=departureTime;
        this.scheduledArrival=arrivalTime;
        this.departureStation=departure;
        this.arrivalStation=arrival;
        this.totalSeats=trainSeats;
        this.trainStatus=TrainStatus.ON_TIME;
        this.seatsPerClass=classSeats;
        this.reservable=reservation;
        this.trainStops=stops;
    }

    public String getTrainID(){return trainID;}
    public String getDepartureStation(){return departureStation;}
    public String getArrivalStation(){return arrivalStation;}
    public LocalDateTime getScheduledDeparture(){return scheduledDeparture;}
    public LocalDateTime getScheduledArrival(){return scheduledArrival;}
    public Integer getAvailableSeatsForClass(TravelClass travelClass){return seatsPerClass.get(travelClass);}
    public Map<TravelClass,Integer> getSeatsPerClass(){return seatsPerClass;}
    public TrainStatus getTrainStatus(){return trainStatus;}
    public TrainType getTrainType(){return trainType;}
    public boolean getReservable(){return reservable;}
    public List<TrainStop> getTrainStop(){return this.trainStops;}
    public int getTotalSeats(){return this.totalSeats;}

   public boolean hasAvailableSeatsForClass(TravelClass travelClass){
        Integer seats= this.seatsPerClass.get(travelClass);
        return seats!= null && seats>0;
   }


}