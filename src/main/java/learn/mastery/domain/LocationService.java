package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.LocationRepository;
import learn.mastery.models.Location;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class LocationService  {

    private final LocationRepository repository;

    public LocationService(LocationRepository repository){
        this.repository = repository;
    }


    public Location findByEmail(String locationEmail) throws FileNotFoundException, DataException {
        return repository.findByEmail(locationEmail);
    }

    public List<Location> findAll() throws DataException {
        return repository.findAll();
    }

    public Location findById(String locationId) throws DataException {

        return repository.findById(locationId);
    }

}
