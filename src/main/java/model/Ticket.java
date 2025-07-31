package model;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Optional;


public class Ticket{
    private final String ticketID;
    private final String departureStation;
    private final String arrivalStation;
    private final LocalDateTime emissionDate;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final LocalDate travelDay;
    private final double finalPrice;
    private final TrainType trainType;
    private final TravelClass travelClass;
    private final Optional<String> assignedSeat;

    public Ticket(Train train, double price,String ID,TravelClass tClass,Optional<String> seat ){
        this.ticketID=ID;
        this.departureStation=train.getDepartureStation();
        this.arrivalStation=train.getArrivalStation();
        this.emissionDate=LocalDateTime.now();
        this.departureTime=train.getScheduledDeparture();
        this.arrivalTime=train.getScheduledArrival();
        this.travelDay=train.getScheduledArrival().toLocalDate();
        this.finalPrice=price;
        this.trainType=train.getTrainType();
        this.travelClass=tClass;
        this.assignedSeat= seat;
    }

    public String getTicketID(){return this.ticketID;}
    public String getDepartureStation(){return this.departureStation;}
    public String getArrivalStation(){return this.arrivalStation;}
    public LocalDateTime getEmissionDate(){return this.emissionDate;}
    public LocalDateTime getDepartureTime(){return this.departureTime;}
    public LocalDateTime getArrivalTime(){return this.arrivalTime;}
    public LocalDate getTravelDay(){return this.travelDay;}
    public double getFinalPrice(){return this.finalPrice;}
    public TrainType getTrainType(){return this.trainType;}
    public TravelClass getTravelClass(){return this.travelClass;}
    public Optional<String> getSeat(){return this.assignedSeat;}
}