package model;

import java.time.LocalDate;
import java.util.List;

import condition.PromotionCondition;

public class Promotion{
    private final String promoID;
    private final PromotionType promoType;
    private final double promoBenefit;
    private final LocalDate promoStartDate;
    private final LocalDate promoEndDate;
    private final List<PromotionCondition> promoConditions;

    public Promotion(String ID, PromotionType type,double benefit, LocalDate startDate,LocalDate endDate,List<PromotionCondition> conditions){
        this.promoID=ID;
        this.promoType=type;
        this.promoBenefit=benefit;
        this.promoStartDate=startDate;
        this.promoEndDate=endDate;
        this.promoConditions=conditions;
    }

    public String getPromoID(){return this.promoID;}
    public PromotionType getPromoType(){return this.promoType;}
    public double getBenefit(){return this.promoBenefit;}
    public LocalDate getPromoStartDate(){return this.promoStartDate;}
    public LocalDate getPromoEndDate(){return this.promoEndDate;}
    public List<PromotionCondition> getPromoConditions(){return this.promoConditions;}

    public boolean isActive(LocalDate date){
        return (date.isEqual(promoStartDate) || date.isAfter(promoStartDate)) && ((date.isEqual(promoEndDate) || date.isBefore(promoEndDate)));
    }

    public boolean isApplicable(Customer customer,Train train,LocalDate date){
        if(!isActive(date))return false;
        return promoConditions.stream().allMatch(cond -> cond.isSatisfiedBy(customer,train,date));
    }
}