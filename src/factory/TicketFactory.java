package factory;

import model.Ticket;
public interface TicketFactory{
        Ticket createTicket(PurchaseTicketRequest);
    }