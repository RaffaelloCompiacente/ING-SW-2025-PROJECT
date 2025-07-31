package strategy;

public class PercentageDiscountStrategy implements PromotionStrategy{
    public double applyPromotion(double promoBenefit,double basePrice){
        return basePrice*(1-promoBenefit);
    }
}