package  model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

public class Voucher{

    private final UUID voucherID;
    private final UUID customerID;
    private final double voucherAmount;
    private final LocalDateTime issuedAt;
    private final Optional<LocalDateTime> expiresAt;
    private boolean used;


    public Voucher(UUID voucherId,UUID customerId,double amount,LocalDateTime expiresDate ){
        this.voucherID=voucherId;
        this.customerID=customerId;
        this.voucherAmount=amount;
        this.issuedAt=LocalDateTime.now();
        this.expiresAt=Optional.of(expiresDate);
        this.used=false;
    }

    public UUID getVoucherID(){return this.voucherID;}
    public UUID getCustomerID(){return this.customerID;}
    public double getVoucherAmount(){return this.voucherAmount;}
    public LocalDateTime getIssuedAt(){return this.issuedAt;}
    public Optional<LocalDateTime> getExpiresAt(){return this.expiresAt;}
    public boolean getIsUsed(){return this.used;}

    public void setIsUsed(){this.used=true;}
}