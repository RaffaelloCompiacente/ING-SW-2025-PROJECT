package dto;


import java.time.LocalDateTime;




public class InternalTrainQuery{
    private final String departureStation;
    private final String arrivalStation;
    private final LocalDateTime departureTresholds;
    private final boolean whithoutConnections;
    private final int passengerCount;
    private final boolean returnTicket;



    public InternalTrainQuery(String departure, String arrival, LocalDateTime dateAndTime, boolean connections, int passenger, boolean returnT){
        this.departureStation=departure;
        this.arrivalStation=arrival;
        this.departureTresholds=dateAndTime;
        this.whithoutConnections=connections;
        this.passengerCount=passenger;
        this.returnTicket=returnT;
    }

    public String getDepartureStation(){return this.departureStation;}
    public String getArrivalStation(){return this.arrivalStation;}
    public LocalDateTime getDepartureTresholds(){return this.departureTresholds;}
    public boolean getWithoutConnections(){return this.whithoutConnections;}
    public int getPassengerCount(){return this.passengerCount;}
    public boolean getReturnTicket(){return this.returnTicket;}








}