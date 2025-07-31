package condition;

import model.Train;
import model.Customer;
import java.time.LocalDate;


public class TrainFidelityCondition implements PromotionCondition{
    private final TrenicalFidelity requiredLevel;

    public FedeltaLevelCondition(TrenicalFidelity level){
        this.requiredLevel=level;
    }

    public boolean isSatisfiedBy(Customer customer,Train train,LocalDate date){
        return customer.getFedeltaLevel().ordinal()>=requiredLevel;
    }

}