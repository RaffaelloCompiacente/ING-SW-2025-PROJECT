package condition;

import model.Customer;
import model.Train;
public interface PromotionCondition{
    boolean isSatisfiedBy(Customer customer,Train train,LocalDate date);
}