package model;


import java.time.LocalDateTime;

public class TrainStop{
    private final String stopStation;
    private final LocalDateTime stopArrivalDate;
    private final LocalDateTime stopDepartureDate;

    public TrainStop(String station, LocalDateTime arrival,LocalDateTime departure){
        this.stopStation=station;
        this.stopArrivalDate=arrival;
        this.stopDepartureDate=departure;
    }

    public String getStopStation(){return stopStation;}
    public LocalDateTime getStopArrivalDate(){return this.stopArrivalDate;}
    public LocalDateTime getStopDepartureDate(){return this.stopDepartureDate;}
}