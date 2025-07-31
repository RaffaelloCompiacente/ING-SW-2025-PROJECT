package strategy;

public class VoucherGenerativeStrategy implements PromotionStrategy{
    public double applyPromotion(double promoBenefit,double basePrice){
        return basePrice*promoBenefit;
    }
}