/*package factory;


import model.Ticket;
import model.TravelClass;


public class ReservedTicketFactory implements TicketFactory{
    @Override
    public Ticket createTicket(PurchaseTicketRequest req,String ID,double price,Optional<String> assignedSeat ) {
        /*Ci sono due situazioni in cui il client può richiedere l'acquisto diretto e sono da previewTicket
        Sia per treni Regionali che Riservabili o dalla prenotazione di un treno Riservabile in entrambi i casi
        nel il DTO della previewTicket ne l'oggetto prenotazione dispongono di ID quindi andrà calcolato da TicketService
        e passato insieme alla richiesta al factory.
        * */
       /* return Ticket(
                req.getTrain(),
                price,
                ID,
                req.getTravelClass(),
                assignedSeat
        )
    }
}*/