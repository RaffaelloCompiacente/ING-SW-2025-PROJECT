/*package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import


public class Reservation{
    private final UUID reservationID;
    private final UUID customerID;
    private final String trainID;
    private final LocalDate departureDate;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final String passengerName;
    private final String passengerSurname;
    private final TravelClass travelClass;
    private final String seatNumber;
    private final LocalDateTime reservationTimeStamp;

    public Reservation(
            UUID reservationId,
            UUID customerId,
            String name,
            String surname,
            String seat,
            TicketPreview ticketPreview
    ){
        this.reservationID=reservationId;
        this.customerID=customerId;
        this.trainID=ticketPreview.getTrainID();
        this.departureDate=ticketPreview.getDepartureDate();
        this.arrivalTime=ticketPreview.getArrivalTime();
        this.departureTime=ticketPreview.getDepartureTime();
        this.passengerName=name;
        this.passengerSurname=surname;
        this.travelClass=ticketPreview.getTravelClass();
        this.seatNumber=seat;
        this.reservationTimeStamp=LocalDateTime.now();
    }


    public UUID getReservationID(){return this.reservationID;}
    public UUID getCustomerID(){return this.customerID;}
    public String getTrainID(){return this.trainID;}
    public LocalDate getDepartureDate(){return this.departureDate;}
    public LocalDateTime getArrivalTime(){return this.arrivalTime;}
    public LocalDateTime getDepartureTime(){return this.departureTime;}
    public String getPassengerName(){return this.passengerName;}
    public String getPassengerSurname(){return this.passengerSurname;}
    public TravelClass getTravelClass(){return this.travelClass;}
    public String getSeatNumber(){return this.seatNumber;}
    public LocalDateTime getReservationTimeStamp(){return this.reservationTimeStamp;}
}*/