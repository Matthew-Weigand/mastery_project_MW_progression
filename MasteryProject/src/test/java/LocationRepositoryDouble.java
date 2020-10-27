import learn.mastery.data.DataException;
import learn.mastery.data.LocationRepository;
import learn.mastery.models.Location;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class LocationRepositoryDouble implements LocationRepository {

    public final static Location LOCATION  = makeLocation();
    private final ArrayList<Location> locations = new ArrayList<>();


    public LocationRepositoryDouble() {
        locations.add(LOCATION);
    }

    @Override
    public Location findByEmail(String locationEmail) throws FileNotFoundException, DataException {
        return null;
    }

    @Override
    public List<Location> findAll() throws DataException {
        return null;
    }

    @Override
    public Location findById(String id) throws DataException {
        return locations.stream()
                .filter(i->i.getLocationId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private static Location makeLocation() {
        Location location = new Location();
        location.setLocationId("./dont-wreck-my-house-data/reservations/6c32ba5f-39c0-40f1-bb96-0441df64eab2.csv");
        location.setStandardRate(200);
        location.setWeekendRate(300);
        return location;
    }
}
