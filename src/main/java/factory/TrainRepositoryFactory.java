package factory;


import repository.TrainRepository;
import repository.impl.DbTrainRepository;
import repository.impl.JsonoTrainRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TrainRepositoryFactory {
    public static TrainRepository createTrainRepository()throws IOException{
        String mode= System.getenv("REPO MODE");

        if("DB".equalsIgnoreCase(mode)){
            try{
                Connection con= DriverManager.getConnection("jdbc:postgresql://localhost:5432/trenical_db","postgres","FeLaFeL!Db!16!");
                return new DbTrainRepository(con);
            }catch(SQLException e){
                throw new RuntimeException("Errore nella connesione al database",e);
            }
        }
        return new JsonoTrainRepository("data/trains.json");
    }

}