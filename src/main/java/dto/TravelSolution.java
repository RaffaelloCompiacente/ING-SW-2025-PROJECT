package dto;


import java.util.List;
import java.util.ArrayList;
import model.Train;

public class TravelSolution{
    private final List<Train> trainList;

    public TravelSolution(List<Train> trains){
        this.trainList=new ArrayList<>(trains);
    }

    public List<Train> getTrainList(){return this.trainList;}
}