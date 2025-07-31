package strategy;


public class FidelityPointsStrategy implements PromotionStrategy{
    public double applyPromotion(double promoBenefit,double basePrice){
        return promoBenefit*basePrice*10;
    }
}