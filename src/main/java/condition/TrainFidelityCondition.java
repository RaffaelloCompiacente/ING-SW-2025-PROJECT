package condition;

import model.Train;
import model.Customer;
import model.TrenicalFidelity;
import java.time.LocalDate;


public class TrainFidelityCondition implements PromotionCondition{
    private final TrenicalFidelity requiredLevel;

    public TrainFidelityCondition(TrenicalFidelity level){
        this.requiredLevel=level;
    }

    public boolean isSatisfiedBy(Customer customer,Train train,LocalDate date){
        return customer.getFidelityLevel()
                .map(level -> level.ordinal() >= requiredLevel.ordinal())
                .orElse(false);
    }

}