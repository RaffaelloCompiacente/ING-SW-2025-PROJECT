package strategy;


public class FixedAmountDiscountStrategy implements PromotionStrategy{
    public double applyPromotion(double promoBenefit,double basePrice){
        return basePrice-promoBenefit;
    }
}