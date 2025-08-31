/*package repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Train;
import repository.TrainRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JsonoTrainRepository implements TrainRepository{
    private final Path jsonPath;
    private final List<Train> trains;

    public JsonoTrainRepository(String filePath)throws IOException{
        this.jsonPath= Paths.get(filePath);
        this.trains=loadTrains();
    }

    private List<Train> loadTrains() throws IOException{
        if(!Files.exists(jsonPath))return new ArrayList<>();
        String json=Files.readString(jsonPath);
        ObjectMapper mapper=new ObjectMapper();
        return Arrays.asList(mapper.readValue(json,Train[].class));
    }

    public void persist() throws IOException{
        ObjectMapper mapper= new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Files.writeString(jsonPath,mapper.writeValueAsString(trains));
    }

    @Override
    public List<Train> getAllTrains(){
        return new ArrayList<>(trains);
    }

    @Override
    public Optional<Train> findById(String trainId){
        return trains.stream().filter(t->t.getTrainID().equals(trainId)).findFirst();
    }

    /*@Override
    public void save(Train train){
        trains.add(train);
    }

    @Override
    public void update(Train train){
        delete(train.getTrainID());
        save(train);
    }

    @Override
    public void delete(String trainId){
        trains.removeIf(t ->t.getTrainID().equals(trainId));
    }
}*/