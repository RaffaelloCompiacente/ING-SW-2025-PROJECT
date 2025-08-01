package model;

import java.util.Set;
import java.util.UUID;
import java.util.Optional;
import java.util.HashSet;


public class Customer{
    private final UUID customerID;
    private String customerName;
    private String customerSurname;
    private String email;
    private String passwordHash;
    private Optional<TrenicalFidelity> fidelityLevel;
    private boolean wantsPromotionalNotifications;
    private Set<String>  followedTrains;

    public Customer(UUID id, String name, String surname,String mail,String password,boolean notificationPreferation){
        this.customerID=id;
        this.customerName=name;
        this.customerSurname=surname;
        this.email=mail;
        this.passwordHash=password;
        this.wantsPromotionalNotifications=notificationPreferation;
        this.followedTrains=new HashSet<>();
        this.fidelityLevel=Optional.empty(); //altrimenti solleva eccezione
    }

    public UUID getCustomerID(){return this.customerID;}
    public String getCustomerName(){return this.customerName;}
    public String getCustomerSurname(){return this.customerSurname;}
    public String getEmail(){return this.email;}
    public String getPasswordHash(){return this.passwordHash;}
    public Optional<TrenicalFidelity> getFidelityLevel(){return this.fidelityLevel;}
    public boolean getPromotionalPreferencies(){return this.wantsPromotionalNotifications;}
    private Set<String> getFollowedTrains(){return this.followedTrains;}

    public void setPromotionalPreferencesOn(){this.wantsPromotionalNotifications=true;}


}