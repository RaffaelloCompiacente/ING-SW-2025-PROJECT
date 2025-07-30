package main.java.dto;

import model.TrainType;
import model.TravelClass;

import java.time.LocalDateTime;
import java.util.Map;

public class TrainCreationRequest{
    private final String trainId;
    private final String departureStation;
    private final String arrivalStation;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final TrainType trainType;
    private final int totalSeats;
    private final Map<TravelClass,Integer> seatsPerClass;

    public TrainCreationRequest(String ID,String dStation,String aStation,LocalDateTime dTime,LocalDateTime aTime,TrainType type,int seats,Map<TravelClass,Integer> seatsClass){
        this.trainId=ID;
        this.departureStation=dStation;
        this.arrivalStation=aStation;
        this.departureTime=dTime;
        this.arrivalTime=aTime;
        this.trainType=type;
        this.totalSeats=seats;
        this.seatsPerClass=seatsClass;
    }

    public String getTrainID(){return this.trainId;}
    public TrainType getTrainType(){return this.trainType;}
    public LocalDateTime getDepartureTime(){return this.departureTime;}
    public LocalDateTime getArrivalTime(){return this.arrivalTime;}
    public String getDepartureStation(){return this.departureStation;}
    public String getArrivalStation(){return this.arrivalStation;}
    public int getTotalSeats(){return this.totalSeats;}
    public Map<TravelClass,Integer> getSeatsPerClass(){return this.seatsPerClass;}





}