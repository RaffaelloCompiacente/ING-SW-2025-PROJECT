package factory;

//Non è il Factory Method ma un simple/registry-based factory

import model.PromotionType;
import strategy.*;
import java.util.Map;

public class PromotionStrategyFactory{
    private final Map<PromotionType, PromotionStrategy> strategyMap= Map.of(
            PromotionType.PERCENTAGE_DISCOUNT,new PercentageDiscountStrategy(),
            PromotionType.FIXED_AMOUNT_DISCOUNT,new FixedAmountDiscountStrategy(),
            PromotionType.VOUCHER_GENERATIVE, new VoucherGenerativeStrategy(),
            PromotionType.FIDELITY_POINT_GENERATIVE, new FidelityPointsStrategy()
    );

    public PromotionStrategy getStrategy(PromotionType promoType){
        return strategyMap.get(promoType);
    }

}