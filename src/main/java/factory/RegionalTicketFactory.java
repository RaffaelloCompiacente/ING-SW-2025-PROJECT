/*
package factory;

import model.Ticket;
import java.util.Optional

public class RegionalTicketFactory implements TicketFactory{
    @Override
    public Ticket createTicket(PurchaseTicketRequest req,String ID,double price,Optional<String> assignedSeat) {
        /*Ci sono due situazioni in cui il client può richiedere l'acquisto diretto e sono da previewTicket
        Sia per treni Regionali che Riservabili o dalla prenotazione di un treno Riservabile in entrambi i casi
        nel il DTO della previewTicket ne l'oggetto prenotazione dispongono di ID quindi andrà calcolato da TicketService
        e passato nella richiesta al factory.
        * */
       /* return new Ticket(
                req.getTrain(),
                price,
                ID,
                "SECONDA",
                null
        );
    }
}*/