package learn.mastery.data;

import learn.mastery.models.Location;

import java.io.FileNotFoundException;
import java.util.List;

public interface LocationRepository {


    Location findByEmail(String locationEmail) throws FileNotFoundException, DataException;

    List<Location> findAll() throws DataException;

   Location findById(String id) throws DataException;



}
