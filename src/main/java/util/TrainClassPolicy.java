package main.java.util;

import main.java.model.TrainType;
import main.java.model.TravelClass;
import java.util.*;

public class TrainClassPolicy{
    private static final Map<TrainType,Set<TravelClass>> ALLOWED_CLASSES= new EnumMap<>(TrainType.class);
    static{
        ALLOWED_CLASSES.put(TrainType.FRECCIAROSSA,EnumSet.of(TravelClass.STANDARD,TravelClass.BUSINESS,TravelClass.EXECUTIVE,TravelClass.PREMIUM));
        ALLOWED_CLASSES.put(TrainType.ITALO,EnumSet.of(TravelClass.SMART,TravelClass.PRIMA,TravelClass.CLUB_EXECUTIVE));
        ALLOWED_CLASSES.put(TrainType.FRECCIABIANCA,EnumSet.of(TravelClass.PRIMA,TravelClass.SECONDA));
        ALLOWED_CLASSES.put(TrainType.FRECCIARGENTO,EnumSet.of(TravelClass.PRIMA,TravelClass.SECONDA));
        ALLOWED_CLASSES.put(TrainType.INTERCITY,EnumSet.of(TravelClass.PRIMA,TravelClass.PRIMA_PLUS,TravelClass.SECONDA));
        ALLOWED_CLASSES.put(TrainType.EUROCITY,EnumSet.of(TravelClass.PRIMA,TravelClass.SECONDA));
        ALLOWED_CLASSES.put(TrainType.REGIONALE,EnumSet.of(TravelClass.SECONDA));
        ALLOWED_CLASSES.put(TrainType.REGIONALE_VELOCE,EnumSet.of(TravelClass.SECONDA));

    }

    public static boolean isClassAllowed(TrainType type, TravelClass travelClass){
        return ALLOWED_CLASSES.getOrDefault(type,Set.of()).contains(travelClass);
    }

    public static void validate(TrainType type,Set<TravelClass> requested){
        Set<TravelClass> allowed =ALLOWED_CLASSES.getOrDefault(type,Set.of());
        for(TravelClass c:requested){
            if(!allowed.contains(c)){
                throw new IllegalArgumentException("La classe "+c+" non è valida per il treno "+type);
            }
        }
    }
}